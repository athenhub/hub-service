package com.athenhub.hubservice.hub.domain.event;

import com.athenhub.hubservice.hub.domain.Hub;
import java.util.UUID;

/**
 * 허브가 삭제되었음을 나타내는 도메인 이벤트.
 *
 * <p>허브 ID는 포함되지 않으며, 허브 관리자 ID와 요청 사용자 정보만 포함된다.
 */
public record HubRouteUpdated(UUID requestId) {

  /**
   * 주어진 {@link Hub} 엔티티로부터 {@code HubDeleted} 이벤트 객체를 생성한다.
   *
   * @return 생성된 {@link HubRouteUpdated} 이벤트 객체
   */
  public static HubRouteUpdated of(UUID requestId) {
    return new HubRouteUpdated(requestId);
  }
}
