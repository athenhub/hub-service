package com.athenhub.hubservice.hub.domain;

import com.athenhub.hubservice.global.domain.AbstractAuditEntity;
import com.athenhub.hubservice.hub.domain.dto.HubRegisterRequest;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import com.athenhub.hubservice.hub.domain.exception.PermissionErrorCode;
import com.athenhub.hubservice.hub.domain.exception.PermissionException;
import com.athenhub.hubservice.hub.domain.service.HubManagerInfoFinder;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import com.athenhub.hubservice.hub.domain.vo.Address;
import com.athenhub.hubservice.hub.domain.vo.Coordinate;
import com.athenhub.hubservice.hub.domain.vo.HubId;
import com.athenhub.hubservice.hub.domain.vo.HubManager;
import com.athenhub.hubservice.hub.domain.vo.HubManagerId;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 허브(Hub) 도메인 엔티티.
 *
 * <p>허브 등록, 조회, 정보 수정, 삭제 기능을 담당하며, 감사(Audit) 기능은 상위 {@link AbstractAuditEntity}를 통해 처리된다.
 *
 * <p>권한 검증 및 회원 존재 여부 확인을 위해 {@link PermissionChecker}, {@link MemberExistenceChecker}를 사용한다. 모든 등록
 * 및 수정, 삭제 시에는 도메인 규칙에 따라 권한과 회원 존재 여부가 검증되며, 위반 시 {@link PermissionException} 또는 {@link
 * IllegalArgumentException}이 발생한다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>id — 허브 식별자({@link HubId})
 *   <li>name — 허브명
 *   <li>address — 주소 정보({@link Address})
 *   <li>coordinate — 위치 정보({@link Coordinate})
 * </ul>
 *
 * <h2>주요 메서드</h2>
 *
 * <ul>
 *   <li>{@link #register(HubRegisterRequest, PermissionChecker, MemberExistenceChecker, UUID)} —
 *       새로운 허브 등록
 *   <li>{@link #updateInfo(HubUpdateRequest, PermissionChecker, UUID)} — 허브 정보 수정
 *   <li>{@link #delete(String, PermissionChecker, UUID)} — 허브 삭제
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Table(name = "p_hub")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends AbstractAuditEntity {

  @EmbeddedId private HubId id;

  private String name;

  @Embedded private Address address;

  @Embedded private Coordinate coordinate;

  @Embedded private HubManagerId managerId;

  /**
   * 허브를 등록한다.
   *
   * <p>등록 시 요청 사용자의 권한과 회원 존재 여부를 검증하며, 검증 실패 시 {@link PermissionException} 또는 {@link
   * IllegalArgumentException}이 발생한다.
   *
   * @param registerRequest 등록 요청 데이터
   * @param permissionChecker 권한 검증 인터페이스
   * @param memberExistenceChecker 회원 존재 여부 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @return 등록된 허브 엔티티
   * @throws NullPointerException 필수 입력 값이 누락된 경우
   * @throws PermissionException 관리 권한이 없는 경우
   * @throws IllegalArgumentException 회원이 존재하지 않는 경우
   */
  public static Hub register(
      HubRegisterRequest registerRequest,
      PermissionChecker permissionChecker,
      MemberExistenceChecker memberExistenceChecker,
      UUID requestId) {
    checkManagePermission(permissionChecker, requestId);
    checkMemberExistence(memberExistenceChecker, requestId);

    Hub hub = new Hub();

    hub.id = HubId.generateId();
    hub.name = Objects.requireNonNull(registerRequest.name());
    hub.address = Address.of(registerRequest.streetAddress(), registerRequest.detailAddress());
    hub.coordinate = Coordinate.of(registerRequest.latitude(), registerRequest.longitude());
    hub.managerId = HubManagerId.of(registerRequest.managerId());

    return hub;
  }

  /**
   * 허브 정보를 수정한다.
   *
   * <p>수정 시 관리 권한을 검증하며, 검증 실패 시 {@link PermissionException}이 발생한다.
   *
   * @param updateRequest 수정 요청 데이터
   * @param permissionChecker 권한 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @throws NullPointerException 필수 입력 값이 누락된 경우
   * @throws PermissionException 관리 권한이 없는 경우
   */
  public void updateInfo(
      HubUpdateRequest updateRequest, PermissionChecker permissionChecker, UUID requestId) {
    checkManagePermission(permissionChecker, requestId);

    this.name = Objects.requireNonNull(updateRequest.name());
    this.address = Address.of(updateRequest.streetAddress(), updateRequest.detailAddress());
    this.coordinate = Coordinate.of(updateRequest.latitude(), updateRequest.longitude());
  }

  /**
   * 허브를 삭제 처리한다.
   *
   * <p>삭제 시 관리 권한을 검증하며, 권한 부족 시 {@link PermissionException}이 발생한다.
   *
   * @param deleteBy 삭제 처리한 회원명
   * @param permissionChecker 권한 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @throws PermissionException 관리 권한이 없는 경우
   */
  public void delete(String deleteBy, PermissionChecker permissionChecker, UUID requestId) {
    checkManagePermission(permissionChecker, requestId);

    super.delete(deleteBy);
  }

  /**
   * 허브 관리자 정보를 조회한다.
   *
   * @param managerFinder 허브 관리자 정보 조회 인터페이스
   * @return 허브 관리자 정보
   */
  public HubManager getManagerInfo(HubManagerInfoFinder managerFinder) {
    return managerFinder.find(this.managerId);
  }

  /**
   * 허브 관리자를 변경한다.
   *
   * @param newManagerId 새로운 허브 관리자 ID
   * @param permissionChecker 권한 검증 인터페이스
   * @param memberExistenceChecker 회원 존재 여부 검증 인터페이스
   * @param requestId 요청자 식별자(UUID)
   * @throws PermissionException 관리 권한이 없는 경우
   * @throws IllegalArgumentException 회원이 존재하지 않는 경우
   */
  public void changeManager(
      UUID newManagerId,
      PermissionChecker permissionChecker,
      MemberExistenceChecker memberExistenceChecker,
      UUID requestId) {
    checkManagePermission(permissionChecker, requestId);
    checkMemberExistence(memberExistenceChecker, requestId);

    this.managerId = HubManagerId.of(newManagerId);
  }

  private static void checkManagePermission(PermissionChecker permissionChecker, UUID requestId) {
    if (!permissionChecker.hasManagePermission(requestId)) {
      throw new PermissionException(PermissionErrorCode.HAS_NOT_MANAGE_PERMISSION);
    }
  }

  private static void checkMemberExistence(
      MemberExistenceChecker memberExistenceChecker, UUID memberId) {
    if (!memberExistenceChecker.hasMember(memberId)) {
      throw new IllegalArgumentException("회원이 존재하지 않습니다. id: " + memberId);
    }
  }
}
