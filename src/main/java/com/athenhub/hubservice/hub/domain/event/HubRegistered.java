package com.athenhub.hubservice.hub.domain.event;

import com.athenhub.hubservice.hub.domain.Hub;
import java.util.UUID;

/**
 * 신규 허브가 등록되었음을 나타하는 도메인 이벤트.
 *
 * <p>허브 ID, 허브명, 관리자 ID 등을 포함하며, 회원 서비스 또는 관련 외부 시스템에서 신규 등록 처리 시 활용된다.
 *
 * @param hubId 등록된 허브 ID
 * @param hubName 등록된 허브명
 * @param hubManagerId 허브 관리자 ID
 * @param requestUsername 요청을 수행한 관리자 계정명
 */
public record HubRegistered(UUID hubId, String hubName, UUID hubManagerId, String requestUsername) {

  /**
   * 주어진 {@link Hub} 엔티티로부터 {@code HubRegistered} 이벤트 객체를 생성한다.
   *
   * @param hub 등록된 허브 엔티티
   * @param requestUsername 요청을 수행한 관리자 계정명
   * @return 생성된 {@link HubRegistered} 이벤트 객체
   */
  public static HubRegistered from(Hub hub, String requestUsername) {
    return new HubRegistered(
        hub.getId().toUuid(), hub.getName(), hub.getManagerId().toUuid(), requestUsername);
  }
}
