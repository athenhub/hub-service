package com.athenhub.hubservice.hub.infrastructure;

import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.infrastructure.client.MemberServiceClient;
import com.athenhub.hubservice.hub.infrastructure.dto.MemberInfo;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 회원 존재 여부를 확인하는 서비스 구현체.
 *
 * <p>이 서비스는 {@link MemberServiceClient}를 통해 외부 회원 서비스에 회원 정보를 조회하고, 회원이 실제 존재하며 삭제되지 않은 상태인지 판단한다.
 *
 * <p>도메인 계층에서 정의한 {@link MemberServiceClient} 인터페이스의 구현체로, 애플리케이션 또는 도메인 서비스 계층에서 회원 유효성 검증을 위해
 * 사용된다.
 */
@Component
@RequiredArgsConstructor
public class MemberExistenceCheckService implements MemberExistenceChecker {

  private final MemberServiceClient memberServiceClient;

  @Override
  public boolean hasMember(UUID memberId) {
    MemberInfo member = memberServiceClient.getMemberInfo(memberId);
    return Objects.nonNull(member) && member.isActivated();
  }
}
