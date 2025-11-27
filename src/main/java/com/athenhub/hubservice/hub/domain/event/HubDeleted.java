package com.athenhub.hubservice.hub.domain.event;

import com.athenhub.hubservice.hub.domain.Hub;
import java.util.UUID;

/**
 * 허브가 삭제되었음을 나타내는 도메인 이벤트.
 *
 * <p>허브 ID는 포함되지 않으며, 허브 관리자 ID와 요청 사용자 정보만 포함된다.
 *
 * @param hubId 삭제된 허브 식별자
 * @param hubManagerId 삭제된 허브의 관리자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record HubDeleted(UUID hubId, UUID hubManagerId, String requestUsername) {

  /**
   * 주어진 {@link Hub} 엔티티로부터 {@code HubDeleted} 이벤트 객체를 생성한다.
   *
   * @param hub 삭제된 허브 엔티티
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link HubDeleted} 이벤트 객체
   */
  public static HubDeleted from(Hub hub, String requestUsername) {
    return new HubDeleted(hub.getId().toUuid(), hub.getManagerId().toUuid(), requestUsername);
  }
}
