package com.athenhub.hubservice.hub.infrastructure;

import com.athenhub.hubservice.hub.application.service.HubMessagePublisher;
import com.athenhub.hubservice.hub.application.service.HubRouteManageService;
import com.athenhub.hubservice.hub.domain.event.HubDeleted;
import com.athenhub.hubservice.hub.domain.event.HubManagerChanged;
import com.athenhub.hubservice.hub.domain.event.HubRegistered;
import com.athenhub.hubservice.hub.domain.event.HubUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 허브(Hub) 관련 도메인 이벤트를 처리하는 이벤트 핸들러.
 *
 * <p>해당 클래스는 허브가 생성, 수정, 삭제될 때 또는 허브 관리자 변경 이벤트가 발생했을 때 그에 따른 후속 처리 로직을 수행한다. 이벤트는 스프링의 {@link
 * TransactionalEventListener}를 통해 트랜잭션 경계에서 수신되며, 모든 이벤트 처리 메서드는 {@link Async} 애너테이션을 통해 비동기적으로
 * 실행된다.
 *
 * <h2>트랜잭션/비동기 처리 구조</h2>
 *
 * <ul>
 *   <li>{@code @TransactionalEventListener}는 트랜잭션 완료 시점(AFTER_COMMIT)에서 이벤트를 수신한다.
 *   <li>{@code @Async} 적용으로 이벤트 처리 로직은 별도 쓰레드에서 실행되며, 본 트랜잭션과 독립적으로 처리된다.
 * </ul>
 *
 * <p>생성자 주입은 {@link RequiredArgsConstructor}를 통해 자동 생성되며, 내부적으로 경로 계산 서비스와 이벤트 발행기를 사용한다.
 *
 * @see HubRegistered
 * @see HubUpdated
 * @see HubDeleted
 * @see HubManagerChanged
 * @see HubRouteManageService
 * @see HubMessagePublisher
 * @author 김형섭
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class HubEventHandler {

  private final HubRouteManageService hubRouteManageService;
  private final HubMessagePublisher hubMessagePublisher;

  /**
   * 허브 등록 이벤트 처리.
   *
   * <p>신규 허브 등록 시 허브 간 경로 데이터를 계산하고, 등록 이벤트를 메시지 브로커에 발행한다.
   *
   * @param event {@link HubRegistered} 이벤트 객체, 등록된 허브의 ID를 포함
   */
  @Async
  @TransactionalEventListener(HubRegistered.class)
  public void handleHubRegistered(HubRegistered event) {
    hubRouteManageService.calculateRoutesForNewHub(event.hubId());
    hubMessagePublisher.publish(event);
  }

  /**
   * 허브 정보 수정 이벤트 처리.
   *
   * <p>허브 정보가 수정될 경우 해당 이벤트를 메시지 브로커에 발행한다.
   *
   * @param event {@link HubUpdated} 이벤트 객체
   */
  @Async
  @TransactionalEventListener(HubUpdated.class)
  public void handleHubUpdated(HubUpdated event) {
    hubMessagePublisher.publish(event);
  }

  /**
   * 허브 삭제 이벤트 처리.
   *
   * <p>허브가 삭제될 경우 관련 경로 데이터를 비활성화하고, 이벤트를 메시지 브로커에 발행한다.
   *
   * @param event {@link HubDeleted} 이벤트 객체, 삭제 대상 허브 ID와 요청자 정보를 포함
   */
  @Async
  @TransactionalEventListener(HubDeleted.class)
  public void handleHubDeleted(HubDeleted event) {
    hubRouteManageService.deactivateRoutesForHub(event.hubId(), event.requestUsername());
    hubMessagePublisher.publish(event);
  }

  /**
   * 허브 관리자 변경 이벤트 처리.
   *
   * <p>허브 관리자 변경 시 변경 이벤트를 메시지 브로커에 발행한다.
   *
   * @param event {@link HubManagerChanged} 이벤트 객체
   */
  @Async
  @TransactionalEventListener(HubManagerChanged.class)
  public void handleHubManagerChanged(HubManagerChanged event) {
    hubMessagePublisher.publish(event);
  }
}
