package com.athenhub.hubservice.hub.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 위치 정보를 표현하는 값 객체(Value Object).
 *
 * <p>위도(latitude), 경도(longitude) 값을 포함하며 JPA 임베디드 타입으로 사용된다. 위치 기반 검색, 거리 계산 등을 수행하는 도메인에서 활용된다.
 *
 * <p>null 값은 허용되지 않으며, null 전달 시 예외가 발생한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Coordinate {
  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  /**
   * 위도와 경도로 Coordinate 객체를 생성한다.
   *
   * @param latitude 위도
   * @param longitude 경도
   * @return 생성된 {@link Coordinate}
   * @throws NullPointerException 위도/경도 입력 값이 누락된 경우
   */
  public static Coordinate of(Double latitude, Double longitude) {
    return new Coordinate(Objects.requireNonNull(latitude), Objects.requireNonNull(longitude));
  }
}
