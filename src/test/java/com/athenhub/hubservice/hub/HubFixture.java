package com.athenhub.hubservice.hub;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubRegisterRequest;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import com.athenhub.hubservice.hub.domain.vo.HubManager;
import com.athenhub.hubservice.hub.domain.vo.HubManagerId;
import java.util.UUID;

/**
 * Hub 관련 테스트 객체 생성을 지원하는 유틸리티 클래스.
 *
 * <p>Hub 도메인을 테스트할 때 반복적으로 필요한 {@link Hub}, {@link HubRegisterRequest}, {@link HubUpdateRequest} 등을
 * 손쉽게 생성할 수 있도록 정형화된 데이터를 제공한다. 랜덤 UUID 및 기본값을 포함하여 테스트 시 안정적인 픽스처를 구성하는 데 활용된다.
 *
 * <h2>제공 기능</h2>
 *
 * <ul>
 *   <li>{@link #createRegisterRequest()} – Hub 등록 요청 DTO 생성
 *   <li>{@link #createRegisterRequest(String, String, String, UUID)} – 지정된 요청 기반
 *   <li>{@link #create(PermissionChecker, MemberExistenceChecker)} – 기본 Hub 엔티티 생성
 *   <li>{@link #create(HubRegisterRequest, PermissionChecker, MemberExistenceChecker)} – 지정된 요청 기반
 *       Hub 생성
 *   <li>{@link #createUpdateRequest()} – Hub 수정 요청 DTO 생성
 *   <li>{@link #getManager(HubManagerId)} - HubManager DTO 생성
 * </ul>
 *
 * <p>이 클래스는 테스트 전용이며, 프로덕션 코드에서는 사용되지 않는다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public class HubFixture {

  private static final UUID requestId = UUID.randomUUID();

  /**
   * 기본 Hub 등록 요청 DTO를 생성한다.
   *
   * @return 미리 정의된 값으로 구성된 {@link HubRegisterRequest}
   */
  public static HubRegisterRequest createRegisterRequest() {
    return createRegisterRequest("서울특별시 센터", "서울특별시 송파구 송파대로 55", "", UUID.randomUUID());
  }

  /**
   * 지정된 값으로 Hub 등록 요청 DTO를 생성한다.
   *
   * <p>테스트 시 특정 값으로 Hub 등록 요청을 만들고자 할 때 사용한다.
   *
   * @param name 등록할 허브명
   * @param streetAddress 도로명 주소
   * @param detailAddress 상세 주소
   * @return 지정된 값으로 구성된 {@link HubRegisterRequest}
   */
  public static HubRegisterRequest createRegisterRequest(
      String name, String streetAddress, String detailAddress, UUID managerId) {
    return new HubRegisterRequest(
        name, streetAddress, detailAddress, 37.489662, 127.032855, managerId);
  }

  /**
   * 기본 Hub 엔티티를 생성한다.
   *
   * <p>내부적으로 {@link #createRegisterRequest()} 를 사용하여 Hub 등록 요청을 생성한 뒤, 도메인 엔티티 생성 메서드인 {@link
   * Hub#register(HubRegisterRequest, PermissionChecker, MemberExistenceChecker, UUID)} 를 호출한다.
   *
   * @param permissionChecker 권한 검증 인터페이스
   * @param memberExistenceChecker 회원 존재 여부 검증 인터페이스
   * @return 생성된 {@link Hub}
   */
  public static Hub create(
      PermissionChecker permissionChecker, MemberExistenceChecker memberExistenceChecker) {
    return create(createRegisterRequest(), permissionChecker, memberExistenceChecker);
  }

  /**
   * 지정된 등록 요청 값을 기반으로 Hub 엔티티를 생성한다.
   *
   * @param request Hub 등록 요청 DTO
   * @param permissionChecker 권한 검증 인터페이스
   * @param memberExistenceChecker 회원 존재 여부 검증 인터페이스
   * @return 생성된 {@link Hub}
   */
  public static Hub create(
      HubRegisterRequest request,
      PermissionChecker permissionChecker,
      MemberExistenceChecker memberExistenceChecker) {
    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    return Hub.register(request, permissionChecker, memberExistenceChecker, requestId);
  }

  /**
   * Hub 수정 요청 DTO를 생성한다.
   *
   * @return Hub 정보 변경을 위한 {@link HubUpdateRequest}
   */
  public static HubUpdateRequest createUpdateRequest() {
    return new HubUpdateRequest("아테네 테크", "서울특별시 강남구 도곡로 113", "", 37.490205, 127.032838);
  }

  /**
   * HubManager DTO를 생성한다.
   *
   * @param managerId 허브 관리자 ID
   * @return 생성된 {@link HubManager}
   */
  public static HubManager getManager(HubManagerId managerId) {
    return new HubManager(managerId, "허브 관리자", "HubManager", "managerSlackId");
  }
}
