package com.athenhub.hubservice.global.infrastructure.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Hub 서비스에서 사용하는 RabbitMQ 설정 정보를 바인딩하는 Properties 클래스이다.
 *
 * <p>{@code application.yml} 또는 {@code application-*.yml}에 정의된 {@code rabbit.hub.*} 설정 값을 객체로 매핑한다.
 *
 * <p>주요 설정 값:
 *
 * <ul>
 *   <li>{@code exchange} : 허브 관련 이벤트를 발행하는 Exchange 이름
 *   <li>{@code queue} : 허브 이벤트를 수신하는 Queue 이름
 *   <li>{@code routingKey} : Exchange → Queue 바인딩에 사용되는 Routing Key
 * </ul>
 *
 * <p>예시:
 *
 * <pre>
 * rabbit:
 *   hub:
 *     exchange: hub.exchange
 *     queue: hub.queue
 *     routing-key: hub.#
 * </pre>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "rabbit.hub")
public class RabbitProperties {
  /** 허브 이벤트를 발행할 Exchange 이름. */
  private String exchange;

  /** 허브 이벤트를 수신할 Queue 이름. */
  private String queue;

  /** Exchange 와 Queue 를 연결할 Routing Key. */
  private String routingKey;
}
