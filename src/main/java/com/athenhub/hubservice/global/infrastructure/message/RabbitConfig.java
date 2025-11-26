package com.athenhub.hubservice.global.infrastructure.message;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 설정.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@RefreshScope
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfig {

  private final RabbitProperties rabbitProperties;

  /** hub 관련 이벤트를 발행하는 Topic Exchange. */
  @Bean
  public TopicExchange hubExchange() {
    return new TopicExchange(rabbitProperties.getExchange(), true, false);
  }

  /** hub 이벤트를 수신할 Queue. */
  @Bean
  public Queue hubQueue() {
    return QueueBuilder.durable(rabbitProperties.getQueue()).build();
  }

  /**
   * Exchange와 Queue를 Binding 한다.
   *
   * <p>routing key를 통해 어떤 메시지를 수신할지 결정된다.
   */
  @Bean
  public Binding hubBinding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(rabbitProperties.getRoutingKey());
  }

  /** JSON 기반 메시지 직렬화 Converter. */
  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
