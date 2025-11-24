package com.athenhub.hubservice.hub.infrastructure;

import com.athenhub.hubservice.hub.application.service.HubManager;
import com.athenhub.hubservice.hub.domain.service.HubManagerInfoFinder;
import com.athenhub.hubservice.hub.domain.vo.HubManagerId;
import com.athenhub.hubservice.hub.domain.vo.HubManagerInfo;
import com.athenhub.hubservice.hub.infrastructure.client.MemberServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 허브 관리자(HubManager) 정보를 조회하는 서비스 구현체.
 *
 * <p>해당 서비스는 외부 회원 서비스(member-service)와 연동하여 허브 관리자 정보를 조회하고, 조회된 회원 정보를 기반으로 {@link HubManager}
 * 도메인 모델로 변환하여 반환한다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>회원 서비스에서 관리자 정보를 조회
 *   <li>조회된 회원 정보를 {@link HubManager}로 매핑
 * </ul>
 *
 * <p>구현체는 {@link HubManagerInfoFinder} 인터페이스를 통해 사용되며, 허브 관리자 관련 기능에서 조회 책임을 담당한다.
 *
 * <p>{@link RequiredArgsConstructor}를 사용하여 필요한 의존성은 생성자 주입으로 관리된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class HubManagerInfoFindService implements HubManagerInfoFinder {

  private final MemberServiceClient memberServiceClient;

  @Override
  public HubManagerInfo find(HubManagerId managerId) {
    return memberServiceClient.getMemberInfo(managerId.toUuid()).toHubManagerInfo();
  }
}
