package com.athenhub.hubservice.hub.domain;

import com.athenhub.hubservice.hub.domain.vo.HubId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * 허브 간 경로(HubRoute) 엔티티에 대한 저장 및 조회 기능을 제공하는 리포지토리 인터페이스.
 *
 * <p>Spring Data Repository 기반으로 구현되며, 허브 간 거리 및 소요 시간 정보를 저장하고 특정 허브와 연관된 모든 경로를 조회하는 기능을 제공한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface HubRouteRepository extends Repository<HubRoute, Long> {

  /**
   * 단일 허브 경로 엔티티를 저장한다.
   *
   * @param route 저장할 {@link HubRoute} 엔티티
   * @return 저장된 엔티티
   */
  HubRoute save(HubRoute route);

  /**
   * 여러 개의 허브 경로 엔티티를 일괄 저장한다.
   *
   * @param routes 저장할 HubRoute 엔티티 목록
   * @param <S> HubRoute의 서브타입
   * @return 저장된 엔티티 목록
   */
  <S extends HubRoute> List<S> saveAll(Iterable<S> routes);

  /**
   * 모든 허브 경로를 조회한다.
   *
   * @return 조회된 {@link HubRoute} 목록
   */
  List<HubRoute> findAll();

  /**
   * ID를 기반으로 단일 허브 경로를 조회한다.
   *
   * @param id 경로 식별자
   * @return 조회된 {@link HubRoute} 엔티티. 존재하지 않을 경우 {@code Optional.empty()}
   */
  Optional<HubRoute> findById(Long id);

  /**
   * 특정 허브가 출발지 또는 도착지로 포함된 모든 경로를 조회한다.
   *
   * <p>허브 간 경로 구성 시 특정 허브와 연결된 모든 경로를 탐색하는 데 사용된다.
   *
   * @param hubId 조회 기준이 되는 허브 ID
   * @return 해당 허브와 연결된 모든 {@link HubRoute} 목록
   */
  @Query(
      """
        SELECT r FROM HubRoute r
        WHERE (
          r.sourceHubId = :hubId
          OR r.targetHubId = :hubId
        )
        AND r.deletedAt IS NULL
      """)
  List<HubRoute> findAllByHubId(HubId hubId);

  /**
   * 활성 상태의 모든 허브 경로를 조회한다.
   *
   * @return 조회된 {@link HubRoute} 목록
   */
  List<HubRoute> findAllByDeletedAtIsNull();
}
