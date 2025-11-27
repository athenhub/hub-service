package com.athenhub.hubservice.hub.application.service;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.HubRoute;
import com.athenhub.hubservice.hub.domain.HubRouteRepository;
import com.athenhub.hubservice.hub.domain.dto.RouteResponse;
import com.athenhub.hubservice.hub.domain.event.HubRouteUpdated;
import com.athenhub.hubservice.hub.domain.service.RouteCalculator;
import com.athenhub.hubservice.hub.domain.vo.HubId;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 허브 간 경로 계산 및 관리를 담당하는 서비스.
 *
 * <p>신규 허브 등록 시 기존 허브와의 모든 경로를 계산하여 저장하고, 허브 비활성화 시 관련 경로를 삭제 처리한다.
 *
 * <h2>주요 역할</h2>
 *
 * <ul>
 *   <li>신규 허브 등록 시 기존 허브와의 양방향 경로 계산 및 저장
 *   <li>허브 비활성화 시 관련 허브 경로 삭제 처리
 * </ul>
 *
 * <p>생성자 주입은 {@link RequiredArgsConstructor}에 의해 자동으로 수행된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class HubRouteService {
  private final HubFinder hubFinder;
  private final HubRouteRepository hubRouteRepository;
  private final RouteCalculator routeCalculator;
  private final HubMessagePublisher hubMessagePublisher;

  /**
   * 신규 허브 등록 시 기존 모든 활성 허브와의 경로를 계산하여 저장한다.
   *
   * @param hubId 신규 허브의 식별자
   */
  public void calculateRoutesForNewHub(UUID hubId) {
    Hub newHub = hubFinder.find(hubId);
    HubId newHubId = newHub.getId();

    List<Hub> hubs = hubFinder.findAllActive();

    // 새로운 hub와 다른 hub 간의 모든 경로 생성
    List<HubRoute> newRoutes =
        hubs.stream()
            .filter(hub -> !hub.getId().equals(newHubId))
            .flatMap(hub -> createBidirectionalRoutes(hub, newHub).stream())
            .toList();

    hubRouteRepository.saveAll(newRoutes);

    hubMessagePublisher.publish(HubRouteUpdated.of(hubId));
  }

  /**
   * 지정한 허브가 출발 허브인 모든 허브 경로를 조회한다.
   *
   * @param hubId 대상 허브의 식별자
   * @return 조회된 허브 경로 목록
   */
  @Transactional(readOnly = true)
  public List<HubRoute> findAllSourceBy(UUID hubId) {
    return hubRouteRepository.findAllByHubId(HubId.of(hubId)).stream()
        .filter(route -> route.getSourceHubId().toUuid().equals(hubId))
        .toList();
  }

  /**
   * 활성상태의 모든 허브 경로를 조회한다.
   *
   * @return 조회된 허브 경로 목록
   */
  @Transactional(readOnly = true)
  public List<HubRoute> findAllByActive() {
    return hubRouteRepository.findAllByDeletedAtIsNull();
  }

  /**
   * 지정한 허브와 관련된 모든 허브 경로를 비활성화 처리한다.
   *
   * @param hubId 대상 허브의 식별자
   * @param deletedBy 삭제 처리 수행자
   */
  public void deactivateRoutesForHub(UUID hubId, String deletedBy) {
    List<HubRoute> routes = hubRouteRepository.findAllByHubId(HubId.of(hubId));
    routes.forEach(route -> route.delete(deletedBy));

    hubRouteRepository.saveAll(routes);

    hubMessagePublisher.publish(HubRouteUpdated.of(hubId));
  }

  /**
   * 두 허브 간의 양방향 경로를 생성한다.
   *
   * @param hub1 허브 1
   * @param hub2 허브 2
   * @return 두 허브 간의 양방향 {@link HubRoute} 리스트
   */
  private List<HubRoute> createBidirectionalRoutes(Hub hub1, Hub hub2) {
    RouteResponse routeTo = routeCalculator.getRoute(hub1.getCoordinate(), hub2.getCoordinate());
    HubRoute route1 =
        HubRoute.create(
            hub1.getId(), hub2.getId(), routeTo.distanceKm(), routeTo.durationMinutes());

    RouteResponse routeFrom = routeCalculator.getRoute(hub2.getCoordinate(), hub1.getCoordinate());
    HubRoute route2 =
        HubRoute.create(
            hub2.getId(), hub1.getId(), routeFrom.distanceKm(), routeFrom.durationMinutes());

    return List.of(route1, route2);
  }
}
