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
 * 주소 정보를 표현하는 값 객체(Value Object).
 *
 * <p>기본 주소(streetAddress)와 상세 주소(detailAddress)를 포함하며, 허브(Hub)의 실제 위치를 정의하기 위해 사용된다.
 *
 * <p>기본 주소는 null이 허용되지 않으며, {@link #of(String, String)} 팩토리 메서드 내부에서 null 검증을 통해 도메인의 일관성을 보장한다. 상세
 * 주소는 선택적으로 제공될 수 있다.
 *
 * <h2>특징</h2>
 *
 * <ul>
 *   <li>JPA @Embeddable 값 타입
 *   <li>불변 객체 생성을 위한 private 생성자
 *   <li>null-safe 팩토리 메서드 제공
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {
  @Column(name = "street_address", nullable = false)
  private String street;

  @Column(name = "detail_address")
  private String detail;

  /**
   * 주소 정보를 생성한다.
   *
   * @param street 기본 주소(필수)
   * @param detailAddress 상세 주소(선택)
   * @return 생성된 {@link Address}
   * @throws NullPointerException address가 null인 경우
   */
  public static Address of(String street, String detailAddress) {
    return new Address(Objects.requireNonNull(street), detailAddress);
  }
}
