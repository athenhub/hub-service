package com.athenhub.hubservice.hub.application.service;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.HubRepository;
import com.athenhub.hubservice.hub.domain.dto.HubRegisterRequest;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import com.athenhub.hubservice.hub.domain.event.HubDeleted;
import com.athenhub.hubservice.hub.domain.event.HubManagerChanged;
import com.athenhub.hubservice.hub.domain.event.HubRegistered;
import com.athenhub.hubservice.hub.domain.event.HubUpdated;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 허브(Hub) 등록 및 관리 기능을 제공하는 서비스 구현체.
 *
 * <p>해당 서비스는 {@link HubRegister} 및 {@link HubManager} 인터페이스를 구현하여 허브 등록, 정보 수정, 삭제 등 쓰기(Write) 작업을
 * 처리한다. 트랜잭션이 필요한 도메인 변경 작업이므로 클래스 전체에 {@code @Transactional}이 적용된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>신규 허브 등록
 *   <li>기존 허브 정보 수정
 *   <li>허브 삭제 처리
 * </ul>
 *
 * <p>{@link Validated} 애너테이션을 통해 메서드 파라미터에 대한 Bean Validation 검증이 수행되며, 서비스 계층에서도 유효성 검사 규칙을 강제한다.
 *
 * <p>생성자 주입은 {@link RequiredArgsConstructor}에 의해 자동 생성된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class HubManageService implements HubRegister, HubManager {

  private final HubRepository hubRepository;
  private final HubFinder hubFinder;
  private final PermissionChecker permissionChecker;
  private final MemberExistenceChecker memberExistenceChecker;
  private final HubEventPublisher hubEventPublisher;

  @Override
  public Hub register(HubRegisterRequest registerRequest, UUID requestId, String requestUsername) {
    Hub hub = Hub.register(registerRequest, permissionChecker, memberExistenceChecker, requestId);

    hub = hubRepository.save(hub);

    hubEventPublisher.publish(HubRegistered.from(hub, requestUsername));

    return hub;
  }

  @Override
  public Hub updateInfo(
      UUID hubId, HubUpdateRequest updateRequest, UUID requestId, String requestUsername) {
    Hub hub = hubFinder.find(hubId);

    hub.updateInfo(updateRequest, permissionChecker, requestId);

    hub = hubRepository.save(hub);

    hubEventPublisher.publish(HubUpdated.from(hub, requestUsername));

    return hub;
  }

  @Override
  public Hub delete(UUID hubId, String deleteBy, UUID requestId, String requestUsername) {
    Hub hub = hubFinder.find(hubId);

    hub.delete(deleteBy, permissionChecker, requestId);

    hub = hubRepository.save(hub);

    hubEventPublisher.publish(HubDeleted.from(hub, requestUsername));

    return hub;
  }

  @Override
  public void changeManager(UUID hubId, UUID newManagerId, UUID requestId, String requestUsername) {
    Hub hub = hubFinder.find(hubId);
    UUID oldManagerId = hub.getManagerId().toUuid();

    hub.changeManager(newManagerId, permissionChecker, memberExistenceChecker, requestId);

    hub = hubRepository.save(hub);

    hubEventPublisher.publish(HubManagerChanged.from(hub, oldManagerId, requestUsername));
  }
}
