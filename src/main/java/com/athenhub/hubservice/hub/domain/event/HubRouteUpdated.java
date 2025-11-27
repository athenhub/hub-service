package com.athenhub.hubservice.hub.domain.event;

import java.util.UUID;

/**
 * 허브 경로가 변경되었음을 나타내는 도메인 이벤트.
 */
public record HubRouteUpdated(UUID requestId) {

  /**
   * 주어진 요청자 ID로 이벤트 객체를 생성한다.
   *
   * @return 생성된 {@link HubRouteUpdated} 이벤트 객체
   */
  public static HubRouteUpdated of(UUID requestId) {
    return new HubRouteUpdated(requestId);
  }
}
