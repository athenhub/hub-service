package com.athenhub.hubservice.hub.application.service;

import com.athenhub.hubservice.hub.domain.event.HubDeleted;
import com.athenhub.hubservice.hub.domain.event.HubManagerChanged;
import com.athenhub.hubservice.hub.domain.event.HubRegistered;
import com.athenhub.hubservice.hub.domain.event.HubUpdated;

/**
 * 허브(Hub) 관련 도메인 이벤트를 발행(publish)하기 위한 인터페이스.
 *
 * <p>해당 인터페이스는 허브 생성, 수정, 삭제, 관리자 변경과 같은 주요 도메인 이벤트를 외부 메시지 브로커(Kafka, RabbitMQ 등) 또는 내부 이벤트 시스템으로
 * 전달하는 역할을 정의한다.
 *
 * <p>이 인터페이스는 구현체에서 실제 이벤트 브로커 연동 방식을 결정하도록 하며, 도메인 계층은 구현 방식에 영향을 받지 않고 이벤트 발행 기능을 사용할 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface HubEventPublisher {
  /**
   * 신규 허브가 등록되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 등록된 허브 정보를 담은 {@link HubRegistered} 이벤트
   */
  void publish(HubRegistered event);

  /**
   * 기존 허브의 정보가 수정되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 수정된 허브 정보를 담은 {@link HubUpdated} 이벤트
   */
  void publish(HubUpdated event);

  /**
   * 허브가 삭제되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 삭제된 허브 정보를 담은 {@link HubDeleted} 이벤트
   */
  void publish(HubDeleted event);

  /**
   * 허브 관리자(HubManager)가 변경되었을 때 발행되는 이벤트를 전송한다.
   *
   * @param event 변경된 관리자 정보를 담은 {@link HubManagerChanged} 이벤트
   */
  void publish(HubManagerChanged event);
}
