package com.athenhub.hubservice.hub.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 * 네이버 지도 Directions5 API의 자동차 경로 탐색 응답을 표현하는 DTO.
 *
 * <p>네이버 길찾기 API는 다양한 경로 옵션(traoptimal, tracomfort 등)을 반환할 수 있으며, 본 클래스는 그 중 대표 경로인 {@code
 * traoptimal} 데이터를 기반으로 거리 및 소요 시간을 제공한다.
 *
 * <p>{@code @JsonIgnoreProperties(ignoreUnknown = true)} 애너테이션을 통해, 응답 필드 구조가 변경되거나 추가되더라도 역직렬화 오류
 * 없이 필요한 필드만 안전하게 파싱할 수 있다.
 *
 * <p>주요 제공 정보:
 *
 * <ul>
 *   <li><b>getDistanceKm()</b> — 총 이동 거리(킬로미터 단위)
 *   <li><b>getDurationMinutes()</b> — 총 소요 시간(분 단위)
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverRouteResponse {
  private Route route;

  /**
   * 네이버 경로 응답으로부터 총 이동 거리를 킬로미터 단위로 반환한다.
   *
   * <p>응답의 첫 번째 traoptimal 경로의 distance 값을 기준으로 하며, 네이버 API는 거리를 미터(m) 단위로 제공하기 때문에 1000으로 나누어 km
   * 단위로 변환한다.
   *
   * @return 이동 거리(km) — traoptimal 경로가 있다면 km 값, 없으면 {@code null}
   */
  public Double getDistanceKm() {
    return this.route.traoptimal.getFirst().summary.distance / 1000.0;
  }

  /**
   * 네이버 경로 응답으로부터 총 소요 시간을 분(Minute) 단위로 반환한다.
   *
   * <p>응답의 첫 번째 traoptimal 경로의 duration 값을 기준으로 하며, 네이버 API는 시간을 밀리초(ms) 단위로 제공하므로 60000(ms → 분)으로
   * 나누어 분 단위로 변환한다.
   *
   * @return 소요 시간(분) — traoptimal 경로가 있다면 분 값, 없으면 {@code null}
   */
  public Integer getDurationMinutes() {
    return this.route.traoptimal.getFirst().summary.duration / 60000;
  }

  /**
   * 경로(route) 정보를 담는 최상위 필드.
   *
   * <p>네이버 Directions API는 여러 경로 옵션을 반환하지만, 본 구조에서는 {@code traoptimal}만 사용한다.
   */
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class Route {
    private List<TraOptimal> traoptimal;
  }

  /**
   * 최적 경로(traoptimal) 옵션 중 하나를 나타내는 데이터 구조.
   *
   * <p>각 항목은 경로의 요약 정보({@link Summary})를 포함한다.
   */
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class TraOptimal {
    private Summary summary;
  }

  /**
   * 경로 요약 정보(Summary)를 담는 데이터 구조.
   *
   * <ul>
   *   <li><b>distance</b> — 총 이동 거리(미터 단위)
   *   <li><b>duration</b> — 총 소요 시간(밀리초 단위)
   * </ul>
   */
  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class Summary {
    private int distance;
    private int duration;
  }
}
