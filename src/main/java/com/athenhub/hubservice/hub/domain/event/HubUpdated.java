package com.athenhub.hubservice.hub.domain.event;

import com.athenhub.hubservice.hub.domain.Hub;
import java.util.UUID;

/**
 * 허브 정보가 수정되었음을 나타하는 도메인 이벤트.
 *
 * <p>업체의 이름 등이 변경되었을 때 발행된다.
 *
 * @param hubName 변경된 허브명
 * @param hubManagerId 허브 관리자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record HubUpdated(String hubName, UUID hubManagerId, String requestUsername) {

  /**
   * 주어진 {@link Hub} 엔티티로부터 {@code HubUpdated} 이벤트 객체를 생성한다.
   *
   * @param hub 수정된 허브 엔티티
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link HubUpdated} 이벤트 객체
   */
  public static HubUpdated from(Hub hub, String requestUsername) {
    return new HubUpdated(hub.getName(), hub.getManagerId().toUuid(), requestUsername);
  }
}
