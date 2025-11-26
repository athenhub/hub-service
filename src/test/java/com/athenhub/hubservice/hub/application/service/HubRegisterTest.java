package com.athenhub.hubservice.hub.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RecordApplicationEvents
@Transactional
class HubRegisterTest {

  @Autowired HubRegister hubRegister;

  @MockitoBean PermissionChecker permissionChecker;

  @MockitoBean MemberExistenceChecker memberExistenceChecker;

  @Autowired ApplicationEvents events;

  @Test
  void register() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any())).thenReturn(true);

    Hub hub =
        hubRegister.register(HubFixture.createRegisterRequest(), UUID.randomUUID(), "requestUser");

    assertThat(hub.getId()).isNotNull();
    assertThat(hub.getManagerId()).isNotNull();
    assertThat(events.stream(HubRegistered.class))
        .hasSize(1)
        .anySatisfy(
            event -> {
              assertThat(event.hubId()).isEqualTo(hub.getId().toUuid());
              assertThat(event.hubName()).isEqualTo(hub.getName());
              assertThat(event.hubManagerId()).isEqualTo(hub.getManagerId().toUuid());
              assertThat(event.requestUsername()).isEqualTo("requestUser");
            });
  }
}
