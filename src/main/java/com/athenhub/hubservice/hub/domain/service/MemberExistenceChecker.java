package com.athenhub.hubservice.hub.domain.service;

import java.util.UUID;

/**
 * 회원(Member)의 존재 여부를 확인하는 도메인 검증 인터페이스.
 *
 * <p>허브 관리자 등록 또는 수정 시 참조하는 회원이 실제로 존재하는지를 확인하기 위한 계약을 정의한다. 도메인 계층은 구현체에 의존하지 않고, 인프라 계층에서 실제 존재
 * 여부를 검증한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface MemberExistenceChecker {

  /**
   * 지정된 회원 식별자에 해당하는 회원이 존재하는지 확인한다.
   *
   * @param memberId 존재 여부를 확인할 회원의 식별자
   * @return 회원이 존재하면 {@code true}, 존재하지 않으면 {@code false}
   */
  boolean hasMember(UUID memberId);
}
