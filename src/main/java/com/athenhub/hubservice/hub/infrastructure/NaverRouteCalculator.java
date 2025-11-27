package com.athenhub.hubservice.hub.infrastructure;

import com.athenhub.hubservice.hub.domain.dto.RouteResponse;
import com.athenhub.hubservice.hub.domain.service.RouteCalculator;
import com.athenhub.hubservice.hub.domain.vo.Coordinate;
import com.athenhub.hubservice.hub.infrastructure.client.NaverDirections5Client;
import com.athenhub.hubservice.hub.infrastructure.dto.NaverRouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 네이버 지도 API를 활용하여 허브 또는 좌표 간 경로(Route)를 계산하는 컴포넌트.
 *
 * <p>{@link RouteCalculator} 인터페이스를 구현하며, 출발지와 목적지 좌표를 기반으로 이동 거리(km)와 소요 시간(분)을 계산하여 {@link
 * RouteResponse} 형태로 반환한다.
 *
 * <p>내부적으로 {@link NaverDirections5Client}를 사용하여 네이버 지도 API의 운전 경로(driving route)를 조회하며, 차량 타입은
 * {@code CAR_TYPE} 상수를 사용한다.
 *
 * <p>Spring 컴포넌트로 등록되어 의존성 주입을 통해 서비스 계층에서 활용 가능하다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class NaverRouteCalculator implements RouteCalculator {

  private static final Integer CAR_TYPE = 3;

  private final NaverDirections5Client naverDirections5Client;

  @Override
  public RouteResponse getRoute(Coordinate source, Coordinate target) {
    String start = source.getLongitude() + "," + source.getLatitude();
    String goal = target.getLongitude() + "," + target.getLatitude();

    NaverRouteResponse route = naverDirections5Client.getRoute(start, goal, CAR_TYPE);

    return new RouteResponse(route.getDistanceKm(), route.getDurationMinutes());
  }
}
