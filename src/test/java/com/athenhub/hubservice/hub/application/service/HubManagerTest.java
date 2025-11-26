package com.athenhub.hubservice.hub.application.service;

import static com.athenhub.hubservice.hub.HubFixture.createRegisterRequest;
import static com.athenhub.hubservice.hub.HubFixture.createUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import com.athenhub.hubservice.hub.domain.event.HubDeleted;
import com.athenhub.hubservice.hub.domain.event.HubManagerChanged;
import com.athenhub.hubservice.hub.domain.event.HubUpdated;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import com.athenhub.hubservice.hub.domain.vo.Address;
import com.athenhub.hubservice.hub.domain.vo.Coordinate;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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
class HubManagerTest {

  @Autowired private HubRegister hubRegister;

  @Autowired private HubManager hubManager;

  @Autowired private HubFinder hubFinder;

  @Autowired private EntityManager entityManager;

  @MockitoBean private PermissionChecker permissionChecker;

  @MockitoBean private MemberExistenceChecker memberExistenceChecker;

  @Autowired ApplicationEvents events;

  Hub hub;

  private final UUID requestId = UUID.randomUUID();
  private final String requestUser = "requestUser";

  @BeforeEach
  void setUp() {
    hub = registerHub();
  }

  @Test
  void updateInfoInfo() {
    HubUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);

    hubManager.updateInfo(hub.getId().toUuid(), request, requestId, requestUser);
    entityManager.flush();
    entityManager.clear();

    hub = hubFinder.find(hub.getId().toUuid());

    assertThat(hub.getName()).isEqualTo(request.name());
    assertThat(hub.getAddress())
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(hub.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
    assertThat(events.stream(HubUpdated.class))
        .hasSize(1)
        .anySatisfy(
            event -> {
              assertThat(event.hubName()).isEqualTo(hub.getName());
              assertThat(event.hubManagerId()).isEqualTo(hub.getManagerId().toUuid());
              assertThat(event.requestUsername()).isEqualTo(requestUser);
            });
  }

  @Test
  void delete() {
    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);

    hubManager.delete(hub.getId().toUuid(), "requestUser", requestId, requestUser);
    entityManager.flush();
    entityManager.clear();

    hub = hubFinder.find(hub.getId().toUuid());

    assertThat(hub.getDeletedBy()).isEqualTo("requestUser");
    assertThat(hub.getDeletedAt()).isNotNull();
    assertThat(events.stream(HubDeleted.class))
        .hasSize(1)
        .anySatisfy(
            event -> {
              assertThat(event.hubId()).isEqualTo(hub.getId().toUuid());
              assertThat(event.hubManagerId()).isEqualTo(hub.getManagerId().toUuid());
              assertThat(event.requestUsername()).isEqualTo(requestUser);
            });
  }

  @Test
  void changeManager() {
    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    UUID newManagerId = UUID.randomUUID();
    hubManager.changeManager(hub.getId().toUuid(), newManagerId, requestId, requestUser);
    entityManager.flush();
    entityManager.clear();

    UUID oldManagerId = hub.getManagerId().toUuid();
    hub = hubFinder.find(hub.getId().toUuid());

    assertThat(hub.getManagerId().toUuid()).isEqualTo(newManagerId);
    assertThat(events.stream(HubManagerChanged.class))
        .hasSize(1)
        .anySatisfy(
            event -> {
              assertThat(event.hubId()).isEqualTo(hub.getId().toUuid());
              assertThat(event.hubName()).isEqualTo(hub.getName());
              assertThat(event.newHubManagerId()).isEqualTo(newManagerId);
              assertThat(event.oldHubManagerId()).isEqualTo(oldManagerId);
              assertThat(event.requestUsername()).isEqualTo(requestUser);
            });
  }

  private Hub registerHub() {
    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    Hub hub = hubRegister.register(createRegisterRequest(), requestId, requestUser);
    entityManager.flush();
    entityManager.clear();

    return hub;
  }
}
