package com.athenhub.hubservice.hub.domain.service;

import com.athenhub.hubservice.hub.domain.dto.RouteResponse;
import com.athenhub.hubservice.hub.domain.vo.Coordinate;

/**
 * 두 좌표 간의 경로 정보를 계산하는 기능을 제공하는 인터페이스.
 *
 * <p>구현체는 네이버, 카카오, 자체 지도 엔진 등 다양한 외부(또는 내부) 경로 계산 시스템을 사용할 수 있으며, 본 인터페이스는 경로 계산 로직을 추상화하여 상위 도메인이
 * 특정 외부 API에 의존하지 않도록 한다.
 *
 * <p>경로 계산 결과는 {@link RouteResponse}를 통해 반환되며, 거리(km)와 소요 시간(분)을 포함한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface RouteCalculator {
  /**
   * 두 좌표 사이의 경로를 계산한다.
   *
   * <p>해당 메서드는 출발지와 도착지 간의 실제 이동 경로를 계산하고, 그 결과를 거리(킬로미터) 및 소요 시간(분) 단위로 정제하여 {@link
   * RouteResponse}로 반환한다.
   *
   * <p>구현체에서 처리할 수 있는 내용 예:
   *
   * <ul>
   *   <li>네이버 지도 Directions API 호출
   *   <li>카카오 내비 API 호출
   *   <li>사내 GIS(Geographic Information System) 엔진 연동
   * </ul>
   *
   * @param source 출발지 좌표
   * @param target 도착지 좌표
   * @return 이동 거리(km)와 소요 시간(분)을 포함한 {@link RouteResponse}
   */
  RouteResponse getRoute(Coordinate source, Coordinate target);
}
