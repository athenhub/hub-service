package com.athenhub.hubservice.hub.infrastructure;

import com.athenhub.hubservice.global.infrastructure.message.RabbitProperties;
import com.athenhub.hubservice.hub.application.service.HubEventPublisher;
import com.athenhub.hubservice.hub.domain.event.HubDeleted;
import com.athenhub.hubservice.hub.domain.event.HubManagerChanged;
import com.athenhub.hubservice.hub.domain.event.HubRegistered;
import com.athenhub.hubservice.hub.domain.event.HubUpdated;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ를 통해 허브 관련 도메인 이벤트를 발행하는 구현체.
 *
 * <p>각 이벤트는 {@link RabbitTemplate}을 사용해 지정된 익스체인지로 전달되며, 라우팅 키는 이벤트 종류에 따라 구분된다. 설정 정보는 {@link
 * RabbitProperties}를 통해 주입된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitProperties.class)
public class HubRabbitMqPublisher implements HubEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;

  @Override
  public void publish(HubRegistered event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "registered", event);
  }

  @Override
  public void publish(HubUpdated event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "updated", event);
  }

  @Override
  public void publish(HubDeleted event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "deleted", event);
  }

  @Override
  public void publish(HubManagerChanged event) {
    rabbitTemplate.convertAndSend(rabbitProperties.getExchange(), "managerChanged", event);
  }
}
