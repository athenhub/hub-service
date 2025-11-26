package com.athenhub.hubservice.hub.application.service;

import static com.athenhub.hubservice.hub.HubFixture.createRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubSearchCondition;
import com.athenhub.hubservice.hub.domain.event.HubDeleted;
import com.athenhub.hubservice.hub.domain.event.HubRegistered;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HubFinderTest {

  @Autowired private HubRegister hubRegister;

  @Autowired private HubFinder hubFinder;

  @Autowired private HubManager hubManager;

  @Autowired private EntityManager entityManager;

  @MockitoBean private PermissionChecker permissionChecker;

  @MockitoBean private MemberExistenceChecker memberExistenceChecker;

  @MockitoBean private HubEventPublisher hubEventPublisher;

  private final UUID requestId = UUID.randomUUID();

  Hub hub1;
  Hub hub2;
  Hub hub3;
  Hub hub4;
  Hub hub5;
  Hub hub6;
  Hub hub7;
  Hub hub8;
  Hub hub9;
  Hub hub10;

  @BeforeAll
  void setUpAll() {
    hub1 = registerHub("테스트 허브1", "서울시 테스트로 1", "1호");
    hub2 = registerHub("테스트 허브2", "서울시 테스트로 1", "1호");
    hub3 = registerHub("테스트 허브3", "인천시 테스트로 1", "1호");
    hub4 = registerHub("아테네 허브4", "서울시 테스트로 1", "1호");
    hub5 = registerHub("아테네 허브5", "인천시 테스트로 1", "1호");
    hub6 = registerHub("테스트 허브6", "인천시 테스트로 1", "1호");
    hub7 = registerHub("테스트 허브7", "서울시 테스트로 1", "A호");
    hub8 = registerHub("테스트 허브8", "서울시 테스트로 1", "A호");
    hub9 = registerHub("테스트 허브9", "서울시 테스트로 1", "A호");
    hub10 = registerHub("테스트 허브10", "서울시 테스트로 1", "A호");

    when(permissionChecker.hasManagePermission(any())).thenReturn(true);
    doNothing().when(hubEventPublisher).publish(any(HubDeleted.class));

    hubManager.delete(hub1.getId().toUuid(), "test", UUID.randomUUID(), "testUser");
    hubManager.delete(hub2.getId().toUuid(), "test", UUID.randomUUID(), "testUser");
  }

  @BeforeEach
  void setUp() {
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("미삭제 필터 테스트")
  void findHubsByNotDeleted() {
    HubSearchCondition condition = HubSearchCondition.of(null, false);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent())
        .containsExactlyInAnyOrder(hub3, hub4, hub5, hub6, hub7, hub8, hub9, hub10);
  }

  @Test
  @DisplayName("이름 검색 테스트")
  void findHubsByName() {
    HubSearchCondition condition = HubSearchCondition.of("아테네", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent()).containsExactlyInAnyOrder(hub4, hub5);
  }

  @Test
  @DisplayName("기본주소 검색 테스트")
  void findHubsByStreetAddress() {
    HubSearchCondition condition = HubSearchCondition.of("인천", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent()).containsExactlyInAnyOrder(hub3, hub5, hub6);
  }

  @Test
  @DisplayName("상세주소 검색 테스트")
  void findHubsByDetailAddress() {
    HubSearchCondition condition = HubSearchCondition.of("A", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent()).containsExactlyInAnyOrder(hub7, hub8, hub9, hub10);
  }

  @Test
  @DisplayName("정렬 테스트")
  void findHubsWithOrder() {
    HubSearchCondition condition = HubSearchCondition.of(null, true);
    Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "name");

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent())
        .containsExactly(hub9, hub8, hub7, hub6, hub3, hub2, hub10, hub1, hub5, hub4);
  }

  @Test
  @DisplayName("페이징 테스트")
  void findHubsWithPaging() {
    HubSearchCondition condition = HubSearchCondition.of(null, true);
    Pageable pageable = PageRequest.of(1, 2, Direction.DESC, "name");

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent()).containsExactly(hub7, hub6);
    assertThat(hubs.getTotalElements()).isEqualTo(10);
    assertThat(hubs.getTotalPages()).isEqualTo(5);
  }

  @Test
  @DisplayName("검색 결과가 없는 경우")
  void findHubsWhenNoMatch() {
    HubSearchCondition condition = HubSearchCondition.of("없는 이름", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent()).isEmpty();
  }

  @Test
  @DisplayName("검색어 대소문자 테스트")
  void findHubsByLetterCase() {
    HubSearchCondition condition = HubSearchCondition.of("a", true);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Hub> hubs = hubFinder.search(condition, pageable);

    assertThat(hubs.getContent()).containsExactlyInAnyOrder(hub7, hub8, hub9, hub10);
  }

  private Hub registerHub(String name, String streetAddress, String detailAddress) {
    when(permissionChecker.hasManagePermission(any(UUID.class))).thenReturn(true);
    when(memberExistenceChecker.hasMember(any(UUID.class))).thenReturn(true);
    doNothing().when(hubEventPublisher).publish(any(HubRegistered.class));

    return hubRegister.register(
        createRegisterRequest(name, streetAddress, detailAddress, UUID.randomUUID()),
        requestId,
        "requestUser");
  }
}
