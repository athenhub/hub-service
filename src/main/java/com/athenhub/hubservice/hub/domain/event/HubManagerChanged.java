package com.athenhub.hubservice.hub.domain.event;

import com.athenhub.hubservice.hub.domain.Hub;
import java.util.UUID;

/**
 * 허브 관리자(HubManager)가 변경되었음을 나타내는 도메인 이벤트.
 *
 * <p>허브 ID, 허브명, 이전 관리자 ID, 새로운 관리자 ID 등을 포함한다. 회원 서비스나 권한 시스템에서 관련 정보 동기화에 사용될 수 있다.
 *
 * @param hubId 허브 ID
 * @param hubName 허브명
 * @param oldHubManagerId 기존 관리자 ID
 * @param newHubManagerId 신규 관리자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record HubManagerChanged(
    UUID hubId,
    String hubName,
    UUID oldHubManagerId,
    UUID newHubManagerId,
    String requestUsername) {
  /**
   * 주어진 {@link Hub} 엔티티로부터 {@code HubManagerChanged} 이벤트 객체를 생성한다.
   *
   * @param hub 관리자가 변경된 허브 엔티티
   * @param oldHubManagerId 변경 이전 허브 관리자 ID
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link HubManagerChanged} 이벤트 객체
   */
  public static HubManagerChanged from(Hub hub, UUID oldHubManagerId, String requestUsername) {
    return new HubManagerChanged(
        hub.getId().toUuid(),
        hub.getName(),
        oldHubManagerId,
        hub.getManagerId().toUuid(),
        requestUsername);
  }
}
