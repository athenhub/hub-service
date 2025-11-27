package com.athenhub.hubservice.presentation.webapi;

import com.athenhub.hubservice.hub.application.service.HubRouteService;
import com.athenhub.hubservice.hub.domain.HubRoute;
import com.athenhub.hubservice.presentation.webapi.dto.HubRouteResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 허브(Hub) 간 경로(Route) 조회를 위한 REST API 컨트롤러.
 *
 * <p>해당 컨트롤러는 특정 허브를 기준으로 출발지 역할을 하는 모든 허브 경로 정보를 조회하는 엔드포인트를 제공한다.
 *
 * <h2>주요 역할</h2>
 *
 * <ul>
 *   <li>허브 ID를 기준으로 출발지 허브 경로 목록 조회
 *   <li>조회된 도메인 엔티티(HubRoute)를 {@link HubRouteResponse} DTO로 변환하여 반환
 * </ul>
 *
 * <p>생성자 주입은 {@link RequiredArgsConstructor}에 의해 자동 생성된다.
 *
 * @see HubRouteService
 * @see HubRouteResponse
 * @author 김형섭
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class HubRouteApi {
  private final HubRouteService hubRouteService;

  /**
   * 특정 허브의 출발지 허브 경로 목록 조회.
   *
   * <p>허브 ID를 기반으로 출발지 역할을 하는 모든 경로 정보를 조회하고, {@link HubRouteResponse} 리스트로 변환하여 반환한다.
   *
   * <p>접근 권한은 {@code MASTER_MANAGER}, {@code HUB_MANAGER}, {@code SHIPPING_AGENT}, {@code
   * VENDOR_AGENT} 역할을 가진 사용자에게 허용된다.
   *
   * @param hubId 조회할 허브의 UUID
   * @return 출발지 허브 경로 목록을 DTO 형태로 반환
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER', 'SHIPPING_AGENT', 'VENDOR_AGENT')")
  @GetMapping("/v1/hubs/{hubId}/routes")
  public List<HubRouteResponse> findAllBy(@PathVariable UUID hubId) {
    List<HubRoute> routes = hubRouteService.findAllSourceBy(hubId);

    return routes.stream().map(HubRouteResponse::from).toList();
  }

  /**
   * 허브 경로 목록 조회.
   *
   * <p>활성 상태의 모든 경로 정보를 조회하고, {@link HubRouteResponse} 리스트로 변환하여 반환한다.
   *
   * <p>접근 권한은 {@code MASTER_MANAGER}, {@code HUB_MANAGER}, {@code SHIPPING_AGENT}, {@code
   * VENDOR_AGENT} 역할을 가진 사용자에게 허용된다.
   *
   * @return 허브 경로 목록을 DTO 형태로 반환
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER', 'SHIPPING_AGENT', 'VENDOR_AGENT')")
  @GetMapping("/v1/routes")
  public List<HubRouteResponse> findAll() {
    List<HubRoute> routes = hubRouteService.findAllByActive();

    return routes.stream().map(HubRouteResponse::from).toList();
  }
}
