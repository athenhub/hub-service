package com.athenhub.hubservice.hub.webapi.dto;

import com.athenhub.hubservice.hub.domain.HubRoute;
import java.util.UUID;

/**
 * 허브 경로 조회 응답 DTO.
 *
 * <p>허브 경로를 조회할 때 반환되는 응답 모델로, 출발/도착 허브 이동 간 거리/시간 정보를 포함한다. 본 레코드는 읽기 전용 구조이며, 컨트롤러 계층에서 API 응답
 * 변환을 위해 사용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>sourceHubId — 출발 허브 식별자(UUID)
 *   <li>targetHubId — 도착 허브 식별자(UUID)
 *   <li>distanceKm — 거리 (Km)
 *   <li>durationMinutes — 시간 (분)
 * </ul>
 *
 * <p>정적 메서드 {@link #from(HubRoute)}를 통해 도메인 엔티티를 쉽게 응답 DTO로 변환할 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record HubRouteResponse(
    UUID sourceHubId, UUID targetHubId, Double distanceKm, Integer durationMinutes) {

  /**
   * 도메인 엔티티 {@link HubRoute}로부터 조회 응답 객체를 생성한다.
   *
   * @param route 조회된 허브 경로 도메인 객체
   * @return {@link HubRouteResponse} 변환 결과
   */
  public static HubRouteResponse from(HubRoute route) {
    return new HubRouteResponse(
        route.getSourceHubId().toUuid(),
        route.getTargetHubId().toUuid(),
        route.getDistanceKm(),
        route.getDurationMinutes());
  }
}
