package com.athenhub.hubservice.presentation.webapi.dto;

import com.athenhub.hubservice.hub.domain.Hub;
import java.util.UUID;

/**
 * 허브 삭제 응답 DTO.
 *
 * <p>허브 삭제 요청 처리 후, 삭제된 허브의 식별자 정보를 클라이언트에 반환하기 위한 응답 전용 레코드 타입이다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>hubId — 삭제된 허브의 식별자(UUID)
 * </ul>
 *
 * <p>{@link #from(Hub)} 정적 팩터리 메서드를 통해 도메인 엔티티 {@link Hub}로부터 쉽게 응답 객체를 생성할 수 있다.
 *
 * @param hubId 삭제된 허브의 UUID
 * @author 김형섭
 * @since 1.0.0
 */
public record HubDeleteResponse(UUID hubId) {
  /**
   * 도메인 엔티티 {@link Hub}로부터 응답 객체를 생성한다.
   *
   * @param hub 삭제된 허브 도메인 객체
   * @return {@link HubDeleteResponse} 생성 결과
   */
  public static HubDeleteResponse from(Hub hub) {
    return new HubDeleteResponse(hub.getId().toUuid());
  }
}
