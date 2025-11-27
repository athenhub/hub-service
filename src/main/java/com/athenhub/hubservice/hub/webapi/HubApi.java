package com.athenhub.hubservice.hub.webapi;

import com.athenhub.commonmvc.security.AuthenticatedUser;
import com.athenhub.hubservice.hub.application.service.HubFinder;
import com.athenhub.hubservice.hub.application.service.HubManager;
import com.athenhub.hubservice.hub.application.service.HubRegister;
import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubRegisterRequest;
import com.athenhub.hubservice.hub.domain.dto.HubSearchCondition;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import com.athenhub.hubservice.hub.domain.vo.HubManagerInfo;
import com.athenhub.hubservice.hub.webapi.dto.HubDeleteResponse;
import com.athenhub.hubservice.hub.webapi.dto.HubFindResponse;
import com.athenhub.hubservice.hub.webapi.dto.HubManagerChangeRequest;
import com.athenhub.hubservice.hub.webapi.dto.HubManagerInfoResponse;
import com.athenhub.hubservice.hub.webapi.dto.HubRegisterResponse;
import com.athenhub.hubservice.hub.webapi.dto.HubUpdateResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 허브(Hub) 관련 REST API 컨트롤러.
 *
 * <p>허브 등록, 조회, 정보 수정, 삭제 기능을 제공하며, 스프링 시큐리티의 역할 기반 접근 제어(@PreAuthorize)를 사용하여 요청 권한을 검증한다.
 *
 * <p>비즈니스 로직은 애플리케이션 계층의 {@link HubRegister}, {@link HubFinder}, {@link HubManager} 인터페이스를 통해 수행된다.
 *
 * <p>각 요청은 인증된 사용자 정보({@link AuthenticatedUser})를 활용해 감사(Audit) 및 권한 확인에 사용된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class HubApi {
  private final HubRegister hubRegister;
  private final HubFinder hubFinder;
  private final HubManager hubManager;

  /**
   * 허브를 신규 등록한다.
   *
   * <p>등록 권한은 MASTER_MANAGER 역할을 가진 사용자만 가능하다.
   *
   * @param requestUser 인증된 사용자 정보
   * @param registerRequest 허브 등록 요청 데이터
   * @return 등록된 허브 정보를 담은 {@link HubRegisterResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER')")
  @PostMapping("/v1/hubs")
  public HubRegisterResponse register(
      @AuthenticationPrincipal AuthenticatedUser requestUser,
      @RequestBody HubRegisterRequest registerRequest) {
    Hub hub = hubRegister.register(registerRequest, requestUser.id(), requestUser.username());

    return HubRegisterResponse.from(hub);
  }

  /**
   * 특정 허브를 ID 기반으로 조회한다.
   *
   * <p>조회 권한은 MASTER_MANAGER, HUB_MANAGER, SHIPPING_AGENT, VENDOR_AGENT가 포함된다.
   *
   * @param hubId 조회할 허브의 식별자(UUID)
   * @return 조회된 허브 정보를 담은 {@link HubFindResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER', 'SHIPPING_AGENT', 'VENDOR_AGENT')")
  @GetMapping("/v1/hubs/{hubId}")
  public HubFindResponse find(@PathVariable UUID hubId) {
    Hub hub = hubFinder.find(hubId);

    return HubFindResponse.from(hub);
  }

  /**
   * 허브 검색 API.
   *
   * <p>해당 엔드포인트는 다양한 검색 조건을 기반으로 허브 목록을 조회한다. 검색 조건은 {@link HubSearchCondition} 으로 전달되며, Spring
   * MVC의 {@link ModelAttribute} 바인딩을 통해 쿼리 파라미터에서 자동 매핑된다.
   *
   * <p>검색 결과는 {@link Pageable} 을 사용하여 페이징 처리되며, 결과는 {@link HubFindResponse} 형태로 매핑된 페이지 객체로 반환된다.
   *
   * <p>조회 권한은 MASTER_MANAGER, HUB_MANAGER, SHIPPING_AGENT, VENDOR_AGENT가 포함된다.
   *
   * @param searchCondition 허브 검색 조건. 쿼리스트링을 통해 전달된 파라미터가 자동으로 바인딩된다.
   * @param pageable 페이징 및 정렬 정보.
   * @return 검색 조건에 부합하는 허브 정보를 {@link HubFindResponse} 형태로 반환하는 페이지 객체.
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER', 'SHIPPING_AGENT', 'VENDOR_AGENT')")
  @GetMapping("/v1/hubs")
  public Page<HubFindResponse> search(
      @ModelAttribute HubSearchCondition searchCondition, Pageable pageable) {
    Page<Hub> hubs = hubFinder.search(searchCondition, pageable);

    return hubs.map(HubFindResponse::from);
  }

  /**
   * 허브 정보를 수정한다.
   *
   * <p>수정 권한은 MASTER_MANAGER 역할만 허용된다.
   *
   * @param requestUser 인증된 사용자 정보
   * @param hubId 수정 대상 허브 ID
   * @param updateRequest 수정 요청 데이터
   * @return 수정된 허브 정보를 담은 {@link HubUpdateResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER')")
  @PutMapping("/v1/hubs/{hubId}")
  public HubUpdateResponse updateInfo(
      @AuthenticationPrincipal AuthenticatedUser requestUser,
      @PathVariable UUID hubId,
      @RequestBody HubUpdateRequest updateRequest) {
    Hub hub = hubManager.updateInfo(hubId, updateRequest, requestUser.id(), requestUser.username());

    return HubUpdateResponse.from(hub);
  }

  /**
   * 허브를 삭제 처리한다.
   *
   * <p>삭제 권한은 MASTER_MANAGER 역할만 허용된다.
   *
   * @param requestUser 인증된 사용자 정보
   * @param hubId 삭제 대상 허브 ID
   * @return 삭제 처리된 허브 정보를 담은 {@link HubDeleteResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER')")
  @DeleteMapping("/v1/hubs/{hubId}")
  public HubDeleteResponse delete(
      @AuthenticationPrincipal AuthenticatedUser requestUser, @PathVariable UUID hubId) {
    Hub hub =
        hubManager.delete(
            hubId, requestUser.getUsername(), requestUser.id(), requestUser.username());

    return HubDeleteResponse.from(hub);
  }

  /**
   * 특정 허브의 관리자를 조회한다.
   *
   * <p>조회 권한은 MASTER_MANAGER, HUB_MANAGER, SHIPPING_AGENT, VENDOR_AGENT가 포함된다.
   *
   * @param hubId 조회할 허브의 식별자(UUID)
   * @return 조회된 허브 관리자 정보를 담은 {@link HubManagerInfoResponse}
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER', 'HUB_MANAGER', 'SHIPPING_AGENT', 'VENDOR_AGENT')")
  @GetMapping("/v1/hubs/{hubId}/manager")
  public HubManagerInfoResponse findManager(@PathVariable UUID hubId) {
    HubManagerInfo manager = hubFinder.findManager(hubId);

    return HubManagerInfoResponse.of(manager);
  }

  /**
   * 특정 허브의 관리자를 변경한다.
   *
   * <p>변경 권한은 MASTER_MANAGER 역할만 허용된다.
   *
   * @param requestUser 인증된 사용자 정보
   * @param hubId 변경할 허브의 식별자(UUID)
   * @param changeRequest 새로운 허브 관리자 ID
   */
  @PreAuthorize("hasAnyRole('MASTER_MANAGER')")
  @PutMapping("/v1/hubs/{hubId}/manager")
  public void changeManager(
      @AuthenticationPrincipal AuthenticatedUser requestUser,
      @PathVariable UUID hubId,
      @RequestBody HubManagerChangeRequest changeRequest) {
    hubManager.changeManager(
        hubId, changeRequest.newMangerId(), requestUser.id(), requestUser.username());
  }
}
