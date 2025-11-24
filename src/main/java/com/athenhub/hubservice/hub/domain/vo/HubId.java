package com.athenhub.hubservice.hub.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 허브 식별자(HubId)를 표현하는 값 객체(Value Object).
 *
 * <p>본 클래스는 UUID 기반의 식별자를 래핑하며, JPA 임베디드 타입으로 사용된다.
 *
 * <h2>생성 규칙</h2>
 *
 * <ul>
 *   <li>{@link #generateId()} — 신규 UUID 기반 HubId 생성
 *   <li>{@link #of(UUID)} — 기존 UUID 값을 사용한 HubId 생성
 * </ul>
 *
 * <p>모든 생성 메서드는 null 입력에 대해 {@link NullPointerException} 을 발생시켜 도메인의 일관성을 보장한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HubId {

  private UUID id;

  /**
   * 새로운 허브 식별자를 생성한다.
   *
   * @return 신규 UUID 기반의 {@link HubId}
   */
  public static HubId generateId() {
    return of(UUID.randomUUID());
  }

  /**
   * 주어진 UUID로 식별자를 생성한다.
   *
   * @param id UUID 값
   * @return {@link HubId} 인스턴스
   * @throws NullPointerException id가 null인 경우
   */
  public static HubId of(UUID id) {
    return new HubId(Objects.requireNonNull(id));
  }

  /**
   * 내부 UUID 값을 반환한다.
   *
   * @return UUID 값
   */
  public UUID toUuid() {
    return id;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
