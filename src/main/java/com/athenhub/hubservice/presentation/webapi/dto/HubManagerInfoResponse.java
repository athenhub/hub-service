package com.athenhub.hubservice.presentation.webapi.dto;

import com.athenhub.hubservice.hub.domain.vo.HubManagerInfo;
import java.util.UUID;

/**
 * 허브 관리자 조회 응답 DTO.
 *
 * <p>특정 허브 관리자를 조회할 때 반환되는 응답 모델로, 허브 관리자의 기본 정보를 포함한다. 본 레코드는 읽기 전용 구조이며, 컨트롤러 계층에서 API 응답 변환을 위해
 * 사용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>id — 허브 관리자 ID
 *   <li>name — 허브 관리자 이름
 *   <li>username — 허브 관리자 계정
 *   <li>slackId — 허브 관리자 슬랙 계정
 * </ul>
 *
 * <p>정적 메서드 {@link #of(HubManagerInfo)}를 통해 {@link HubManagerInfo}를 쉽게 응답 DTO로 변환할 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record HubManagerInfoResponse(UUID id, String name, String username, String slackId) {

  /**
   * {@link HubManagerInfo}로부터 조회 응답 객체를 생성한다.
   *
   * @param manager 조회된 허브 도메인 객체
   * @return {@link HubManagerInfoResponse} 변환 결과
   */
  public static HubManagerInfoResponse of(HubManagerInfo manager) {
    return new HubManagerInfoResponse(
        manager.id().toUuid(), manager.name(), manager.username(), manager.slackId());
  }
}
