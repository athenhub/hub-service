package com.athenhub.hubservice.hub.infrastructure.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 네이버 지도 API 호출을 위한 Feign 클라이언트 설정 클래스.
 *
 * <p>Feign 클라이언트 요청 시 네이버 API 인증 헤더를 자동으로 추가하기 위해 {@link RequestInterceptor} 빈을 정의한다.
 *
 * <p>application.properties 또는 application.yml에 정의된 아래 프로퍼티를 사용한다:
 *
 * <ul>
 *   <li>{@code naver.api.key-id} — 네이버 클라우드 플랫폼 API 키 ID
 *   <li>{@code naver.api.key} — 네이버 클라우드 플랫폼 API 키
 * </ul>
 *
 * <p>이 설정은 {@link NaverDirections5Client} Feign 클라이언트와 함께 사용된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Configuration
public class NaverFeignConfig {

  @Value("${naver.api.key-id}")
  private String apiKeyId;

  @Value("${naver.api.key}")
  private String apiKey;

  /**
   * 네이버 지도 API 호출 시 인증 헤더를 추가하는 {@link RequestInterceptor} 빈을 생성한다.
   *
   * <p>Feign 요청마다 아래 헤더가 자동으로 포함된다:
   *
   * <ul>
   *   <li>{@code x-ncp-apigw-api-key-id} — API 키 ID
   *   <li>{@code x-ncp-apigw-api-key} — API 키
   * </ul>
   *
   * @return 네이버 API 요청용 {@link RequestInterceptor} 빈
   */
  @Bean
  public RequestInterceptor naverApiInterceptor() {
    return requestTemplate -> {
      requestTemplate.header("x-ncp-apigw-api-key-id", apiKeyId);
      requestTemplate.header("x-ncp-apigw-api-key", apiKey);
    };
  }
}
