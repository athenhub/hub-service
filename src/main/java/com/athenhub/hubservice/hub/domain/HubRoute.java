package com.athenhub.hubservice.hub.domain;

import com.athenhub.hubservice.global.domain.AbstractAuditEntity;
import com.athenhub.hubservice.hub.domain.vo.HubId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * 허브 간 이동 경로(Route) 정보를 저장하는 엔티티.
 *
 * <p>각 경로는 출발 허브와 도착 허브의 식별자, 그리고 두 지점 사이의 이동 거리(km) 및 예상 소요 시간(분)을 포함한다. 이 정보는 외부 지도 API(예: 네이버
 * Directions API) 또는 사내 경로 산출 로직을 통해 계산된 값을 기반으로 데이터베이스에 저장된다.
 *
 * <p>본 엔티티는 허브 간 배송 경로 최적화, 배송 시간 예측, 경로 추천 등의 기능에 활용된다.
 *
 * <h2>구성 필드</h2>
 *
 * <ul>
 *   <li>{@code id} — 경로 식별자(PK)
 *   <li>{@code sourceHubId} — 출발 허브의 식별자
 *   <li>{@code targetHubId} — 도착 허브의 식별자
 *   <li>{@code distanceKm} — 허브 간 거리(km 단위)
 *   <li>{@code durationMinutes} — 이동 예상 소요 시간(분 단위)
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Table(name = "p_hub_route")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRoute extends AbstractAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "source_hub_id", nullable = false))
  private HubId sourceHubId;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "target_hub_id", nullable = false))
  private HubId targetHubId;

  private Double distanceKm;

  private Integer durationMinutes;

  /**
   * 출발 허브와 도착 허브 간의 경로 정보를 기반으로 {@link HubRoute} 엔티티를 생성한다.
   *
   * <p>거리(km) 및 예상 소요 시간(분)은 외부 경로 계산 API 또는 내부 로직을 통해 계산된 값이어야 하며, 이 메서드는 해당 값을 사용하여 불변성 검증(Null
   * 체크)을 수행한 후 엔티티를 생성한다.
   *
   * @param source 출발 허브 ID (null 불가)
   * @param target 도착 허브 ID (null 불가)
   * @param distanceKm 허브 간 거리(km). null 불가
   * @param durationMinutes 허브 간 예상 소요 시간(분). null 불가
   * @return 생성된 {@link HubRoute} 엔티티
   * @throws NullPointerException 제공된 파라미터 중 하나라도 null일 경우 발생
   */
  public static HubRoute create(
      HubId source, HubId target, Double distanceKm, Integer durationMinutes) {
    HubRoute route = new HubRoute();

    route.sourceHubId = Objects.requireNonNull(source);
    route.targetHubId = Objects.requireNonNull(target);
    route.distanceKm = Objects.requireNonNull(distanceKm);
    route.durationMinutes = Objects.requireNonNull(durationMinutes);

    return route;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    HubRoute that = (HubRoute) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
