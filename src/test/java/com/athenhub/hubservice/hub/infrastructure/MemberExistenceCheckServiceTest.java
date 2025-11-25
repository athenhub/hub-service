package com.athenhub.hubservice.hub.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.infrastructure.client.MemberServiceClient;
import com.athenhub.hubservice.hub.infrastructure.dto.MemberInfo;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberExistenceCheckServiceTest {
  @Mock MemberServiceClient memberServiceClient;

  @InjectMocks MemberExistenceCheckService memberExistenceCheckService;

  @Test
  void memberExistsReturnTrue() {
    MemberInfo member =
        createMemberInfo(
            UUID.randomUUID(), MemberRole.HUB_MANAGER, MemberStatus.ACTIVATED, null, true);

    when(memberServiceClient.getMemberInfo(member.id())).thenReturn(member);

    assertThat(memberExistenceCheckService.hasMember(member.id())).isTrue();
  }

  @Test
  void memberNotExistsReturnFalse() {
    UUID memberId = UUID.randomUUID();
    when(memberServiceClient.getMemberInfo(memberId)).thenReturn(null);

    assertThat(memberExistenceCheckService.hasMember(memberId)).isFalse();
  }

  @Test
  void memberDeactivatedReturnFalse() {
    MemberInfo member =
        createMemberInfo(
            UUID.randomUUID(), MemberRole.MASTER_MANAGER, MemberStatus.DEACTIVATED, null, false);

    when(memberServiceClient.getMemberInfo(member.id())).thenReturn(member);

    assertThat(memberExistenceCheckService.hasMember(member.id())).isFalse();
  }

  private static MemberInfo createMemberInfo(
      UUID memberId,
      MemberRole role,
      MemberStatus status,
      LocalDateTime deletedAt,
      boolean isActivated) {
    return new MemberInfo(
        memberId,
        "테스트 회원",
        "testMember",
        "testSlackId",
        "서울 물류",
        role,
        status,
        LocalDateTime.now(),
        LocalDateTime.now(),
        deletedAt,
        null,
        isActivated);
  }
}
