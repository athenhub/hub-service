package com.athenhub.hubservice.hub.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
class PermissionCheckServiceTest {

  @Mock MemberServiceClient memberServiceClient;

  @InjectMocks PermissionCheckService permissionCheckService;

  @Test
  void ifActiveMasterManagerReturnTrue() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, null);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(permissionCheckService.hasManagePermission(memberInfo.id())).isTrue();
  }

  @Test
  void ifInActiveMasterManagerReturnFalse() {
    MemberInfo memberInfo =
        createMemberInfo(UUID.randomUUID(), MemberRole.MASTER_MANAGER, LocalDateTime.now());

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(permissionCheckService.hasManagePermission(memberInfo.id())).isFalse();
  }

  @Test
  void ifNotMasterManagerReturnFalse() {
    MemberInfo memberInfo = createMemberInfo(UUID.randomUUID(), MemberRole.HUB_MANAGER, null);

    when(memberServiceClient.getMemberInfo(memberInfo.id())).thenReturn(memberInfo);

    assertThat(permissionCheckService.hasManagePermission(memberInfo.id())).isFalse();
  }

  private static MemberInfo createMemberInfo(
      UUID memberId, MemberRole role, LocalDateTime deletedAt) {
    return new MemberInfo(
        memberId,
        "테스트 회원",
        "testMember",
        "testSlackId",
        "서울 물류",
        role,
        "ACTIVATE",
        LocalDateTime.now(),
        LocalDateTime.now(),
        deletedAt,
        null);
  }
}
