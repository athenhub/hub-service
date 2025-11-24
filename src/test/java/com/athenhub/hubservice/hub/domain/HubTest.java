package com.athenhub.hubservice.hub.domain;

import static com.athenhub.hubservice.hub.HubFixture.create;
import static com.athenhub.hubservice.hub.HubFixture.createRegisterRequest;
import static com.athenhub.hubservice.hub.HubFixture.createUpdateRequest;
import static com.athenhub.hubservice.hub.HubFixture.getManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.domain.dto.HubRegisterRequest;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import com.athenhub.hubservice.hub.domain.exception.PermissionException;
import com.athenhub.hubservice.hub.domain.service.HubManagerInfoFinder;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import com.athenhub.hubservice.hub.domain.vo.Address;
import com.athenhub.hubservice.hub.domain.vo.Coordinate;
import com.athenhub.hubservice.hub.domain.vo.HubManager;
import com.athenhub.hubservice.hub.domain.vo.HubManagerId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HubTest {

  @Mock PermissionChecker permissionChecker;

  @Mock MemberExistenceChecker memberExistenceChecker;

  @Mock HubManagerInfoFinder hubManagerInfoFinder;

  Hub hub;

  private final UUID requestId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    hub = create(permissionChecker, memberExistenceChecker);
  }

  @Test
  void register() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any())).thenReturn(true);

    HubRegisterRequest request = createRegisterRequest();

    Hub hub = Hub.register(request, permissionChecker, memberExistenceChecker, requestId);

    assertThat(hub.getId()).isNotNull();
    assertThat(hub.getName()).isEqualTo(request.name());
    assertThat(hub.getAddress())
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(hub.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
    assertThat(hub.getManagerId()).isEqualTo(HubManagerId.of(request.managerId()));
  }

  @Test
  void registerHasNotPermission() {
    HubRegisterRequest request = createRegisterRequest();

    when(permissionChecker.hasManagePermission(any())).thenReturn(false);

    assertThatThrownBy(
            () -> Hub.register(request, permissionChecker, memberExistenceChecker, requestId))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void registerIfMemberNotExists() {
    HubRegisterRequest request = createRegisterRequest();

    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any())).thenReturn(false);

    assertThatThrownBy(
            () -> Hub.register(request, permissionChecker, memberExistenceChecker, requestId))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void updateInfo() {
    HubUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any())).thenReturn(true);

    hub.updateInfo(request, permissionChecker, requestId);

    assertThat(hub.getName()).isEqualTo(request.name());
    assertThat(hub.getAddress())
        .isEqualTo(Address.of(request.streetAddress(), request.detailAddress()));
    assertThat(hub.getCoordinate())
        .isEqualTo(Coordinate.of(request.latitude(), request.longitude()));
  }

  @Test
  void updateInfoHasNotPermission() {
    HubUpdateRequest request = createUpdateRequest();

    when(permissionChecker.hasManagePermission(any())).thenReturn(false);

    assertThatThrownBy(() -> hub.updateInfo(request, permissionChecker, requestId))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void delete() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(true);

    hub.delete("test", permissionChecker, requestId);

    assertThat(hub.getDeletedAt()).isNotNull();
    assertThat(hub.getDeletedBy()).isEqualTo("test");
  }

  @Test
  void deleteHasNotPermission() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(false);

    assertThatThrownBy(() -> hub.delete("test", permissionChecker, requestId))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void getHubManagerInfo() {
    HubManager manager = getManager(hub.getManagerId());
    when(hubManagerInfoFinder.find(any())).thenReturn(manager);

    assertThat(hub.getManagerInfo(hubManagerInfoFinder)).isEqualTo(manager);
  }

  @Test
  void changeManager() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any())).thenReturn(true);
    UUID newManagerId = UUID.randomUUID();

    hub.changeManager(newManagerId, permissionChecker, memberExistenceChecker, requestId);

    assertThat(hub.getManagerId()).isEqualTo(HubManagerId.of(newManagerId));
  }

  @Test
  void changeManagerHasNotPermission() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(false);

    assertThatThrownBy(
            () ->
                hub.changeManager(
                    UUID.randomUUID(), permissionChecker, memberExistenceChecker, requestId))
        .isInstanceOf(PermissionException.class);
  }

  @Test
  void changeManagerIfMemberNotExists() {
    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    when(memberExistenceChecker.hasMember(any())).thenReturn(false);

    assertThatThrownBy(
            () ->
                hub.changeManager(
                    UUID.randomUUID(), permissionChecker, memberExistenceChecker, requestId))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
