package com.athenhub.hubservice.hub.domain.service;

import com.athenhub.hubservice.hub.domain.vo.HubManagerId;
import com.athenhub.hubservice.hub.domain.vo.HubManagerInfo;

/**
 * 허브 관리자(HubManager) 정보를 조회하는 기능을 제공하는 조회 전용 인터페이스.
 *
 * <p>구현체는 저장소 또는 외부 서비스에서 관리자 정보를 조회하는 책임을 가진다. 일반적으로 읽기(Read) 전용 유스케이스에서 사용된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>관리자 ID를 기반으로 관리자 정보를 조회
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface HubManagerInfoFinder {
  /**
   * 관리자 ID에 해당하는 허브 관리자 정보를 조회한다.
   *
   * @param managerId 조회할 관리자 ID
   * @return 조회된 {@link HubManagerInfo} 정보
   */
  HubManagerInfo find(HubManagerId managerId);
}
