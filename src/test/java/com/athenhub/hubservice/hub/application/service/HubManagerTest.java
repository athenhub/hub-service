package com.athenhub.hubservice.hub.application.service;

import static com.athenhub.hubservice.hub.HubFixture.createRegisterRequest;
import static com.athenhub.hubservice.hub.HubFixture.createUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class HubManagerTest {

  @Autowired private HubRegister hubRegister;

  @Autowired private HubManager hubManager;

  @Autowired private HubFinder hubFinder;

  @Autowired private EntityManager entityManager;

  @MockitoBean private PermissionChecker permissionChecker;

  @MockitoBean private MemberExistenceChecker memberExistenceChecker;

  Hub hub;

  private final UUID requestId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    hub = registerHub();
  }

  @Test
  void updateInfoInfo() {
    HubUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);

    hubManager.updateInfo(hub.getId().toUuid(), request, requestId);
    entityManager.flush();
    entityManager.clear();

    hub = hubFinder.find(hub.getId().toUuid());

    assertThat(hub.getName()).isEqualTo(request.name());
    assertThat(hub.getAddress())
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(hub.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
  }

  @Test
  void delete() {
    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);

    hubManager.delete(hub.getId().toUuid(), "requestUser", requestId);
    entityManager.flush();
    entityManager.clear();

    hub = hubFinder.find(hub.getId().toUuid());

    assertThat(hub.getDeletedBy()).isEqualTo("requestUser");
    assertThat(hub.getDeletedAt()).isNotNull();
  }

  @Test
  void changeManager() {
    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    UUID newManagerId = UUID.randomUUID();
    hubManager.changeManager(hub.getId().toUuid(), newManagerId, requestId);
    entityManager.flush();
    entityManager.clear();

    hub = hubFinder.find(hub.getId().toUuid());

    assertThat(hub.getManagerId().toUuid()).isEqualTo(newManagerId);
  }

  private Hub registerHub() {
    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);

    Hub hub = hubRegister.register(createRegisterRequest(), requestId);
    entityManager.flush();
    entityManager.clear();

    return hub;
  }
}
