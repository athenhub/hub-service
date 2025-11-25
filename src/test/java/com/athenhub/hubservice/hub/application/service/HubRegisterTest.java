package com.athenhub.hubservice.hub.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.HubFixture;
import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.event.HubRegistered;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class HubRegisterTest {
  @Autowired HubRegister hubRegister;

  @MockitoBean PermissionChecker permissionChecker;

  @MockitoBean MemberExistenceChecker memberExistenceChecker;

  @MockitoBean HubEventPublisher hubEventPublisher;

  @Test
  void register() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any())).thenReturn(true);
    doNothing().when(hubEventPublisher).publish(any(HubRegistered.class));

    Hub hub =
        hubRegister.register(HubFixture.createRegisterRequest(), UUID.randomUUID(), "requestUser");

    assertThat(hub.getId()).isNotNull();
    assertThat(hub.getManagerId()).isNotNull();
    verify(hubEventPublisher).publish(any(HubRegistered.class));
  }
}
