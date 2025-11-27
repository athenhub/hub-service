package com.athenhub.hubservice.hub.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.HubFixture;
import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.HubRepository;
import com.athenhub.hubservice.hub.domain.HubRoute;
import com.athenhub.hubservice.hub.domain.HubRouteRepository;
import com.athenhub.hubservice.hub.domain.dto.RouteResponse;
import com.athenhub.hubservice.hub.domain.event.HubRouteUpdated;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import com.athenhub.hubservice.hub.domain.service.RouteCalculator;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class HubRouteServiceTest {

  @Autowired private HubRouteService hubRouteService;
  @Autowired private HubFinder hubFinder;
  @Autowired private HubRepository hubRepository;
  @Autowired private HubRouteRepository hubRouteRepository;
  @Autowired private EntityManager entityManager;

  @MockitoBean private RouteCalculator routeCalculator;
  @MockitoBean private HubMessagePublisher hubMessagePublisher;

  private Hub hub1;
  private Hub hub2;

  @BeforeEach
  void setUp() {
    hub1 =
        hubRepository.save(
            Hub.register(
                HubFixture.createRegisterRequest(),
                permissionChecker(),
                memberExistenceChecker(),
                UUID.randomUUID()));
    hub2 =
        hubRepository.save(
            Hub.register(
                HubFixture.createRegisterRequest(),
                permissionChecker(),
                memberExistenceChecker(),
                UUID.randomUUID()));
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void calculateRoutesForNewHub_createsBidirectionalRoutes() {
    when(routeCalculator.getRoute(any(), any()))
        .thenReturn(new RouteResponse(10.0, 15))
        .thenReturn(new RouteResponse(10.0, 15));
    doNothing().when(hubMessagePublisher).publish(any(HubRouteUpdated.class));

    Hub newHub =
        hubRepository.save(
            Hub.register(
                HubFixture.createRegisterRequest(),
                permissionChecker(),
                memberExistenceChecker(),
                UUID.randomUUID()));
    entityManager.flush();
    entityManager.clear();

    hubRouteService.calculateRoutesForNewHub(newHub.getId().toUuid());
    entityManager.flush();
    entityManager.clear();

    List<HubRoute> routes = hubRouteRepository.findAll();
    assertThat(routes).hasSize(4); // 2 existing hubs * 2 bidirectional routes
    assertThat(routes).allSatisfy(route -> assertThat(route.getDistanceKm()).isEqualTo(10.0));
    assertThat(routes).allSatisfy(route -> assertThat(route.getDurationMinutes()).isEqualTo(15));
    verify(hubMessagePublisher).publish(any(HubRouteUpdated.class));
  }

  @Test
  void findAllSourceBy_returnsOnlySourceRoutes() {
    HubRoute route1 = HubRoute.create(hub1.getId(), hub2.getId(), 5.0, 10);
    HubRoute route2 = HubRoute.create(hub2.getId(), hub1.getId(), 5.0, 10);
    hubRouteRepository.saveAll(List.of(route1, route2));
    entityManager.flush();
    entityManager.clear();

    List<HubRoute> routes = hubRouteService.findAllSourceBy(hub1.getId().toUuid());

    assertThat(routes).hasSize(1);
    assertThat(routes.getFirst().getSourceHubId()).isEqualTo(hub1.getId());
  }

  @Test
  void deactivateRoutesForHub_marksRoutesAsDeleted() {
    HubRoute route1 = HubRoute.create(hub1.getId(), hub2.getId(), 5.0, 10);
    hubRouteRepository.save(route1);
    entityManager.flush();
    entityManager.clear();
    doNothing().when(hubMessagePublisher).publish(any(HubRouteUpdated.class));

    hubRouteService.deactivateRoutesForHub(hub1.getId().toUuid(), "deletedByUser");
    entityManager.flush();
    entityManager.clear();

    HubRoute route = hubRouteRepository.findById(route1.getId()).orElseThrow();
    assertThat(route.getDeletedBy()).isEqualTo("deletedByUser");
    assertThat(route.getDeletedAt()).isNotNull();
    verify(hubMessagePublisher).publish(any(HubRouteUpdated.class));
  }

  private PermissionChecker permissionChecker() {
    PermissionChecker checker = mock(PermissionChecker.class);
    when(checker.hasManagePermission(any(UUID.class))).thenReturn(true);
    return checker;
  }

  private MemberExistenceChecker memberExistenceChecker() {
    MemberExistenceChecker checker = mock(MemberExistenceChecker.class);
    when(checker.hasMember(any(UUID.class))).thenReturn(true);
    return checker;
  }
}
