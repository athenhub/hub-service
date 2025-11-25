package com.athenhub.hubservice.presentation.webapi;

import static com.athenhub.hubservice.AssertThatUtils.isEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.athenhub.hubservice.MockUser;
import com.athenhub.hubservice.hub.HubFixture;
import com.athenhub.hubservice.hub.application.service.HubFinder;
import com.athenhub.hubservice.hub.application.service.HubManager;
import com.athenhub.hubservice.hub.application.service.HubRegister;
import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubRegisterRequest;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import com.athenhub.hubservice.hub.domain.service.MemberExistenceChecker;
import com.athenhub.hubservice.hub.domain.service.PermissionChecker;
import com.athenhub.hubservice.hub.domain.vo.HubManagerInfo;
import com.athenhub.hubservice.presentation.webapi.dto.HubManagerChangeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@SpringBootTest
@AutoConfigureMockMvc
class HubApiTest {

  @Autowired MockMvcTester mvcTester;

  @Autowired ObjectMapper objectMapper;

  @MockitoBean HubRegister hubRegister;

  @MockitoBean HubFinder hubFinder;

  @MockitoBean HubManager hubManager;

  @MockitoBean PermissionChecker permissionChecker;

  @MockitoBean MemberExistenceChecker memberExistenceChecker;

  Hub hub;

  @BeforeEach
  void setUp() {
    hub = HubFixture.create(permissionChecker, memberExistenceChecker);
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void register() throws JsonProcessingException {
    HubRegisterRequest request = HubFixture.createRegisterRequest();

    hub = HubFixture.create(request, permissionChecker, memberExistenceChecker);
    given(hubRegister.register(any(HubRegisterRequest.class), any())).willReturn(hub);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/v1/hubs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.hubId", isEqualTo(hub.getId().toString()));
  }

  @Test
  @MockUser(roles = "VENDOR_AGENT")
  void registerIfUnauthorized() throws JsonProcessingException {
    HubRegisterRequest request = HubFixture.createRegisterRequest();

    Hub hub = HubFixture.create(request, permissionChecker, memberExistenceChecker);
    given(hubRegister.register(any(HubRegisterRequest.class), any())).willReturn(hub);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/v1/hubs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void find() {
    Hub hub = HubFixture.create(permissionChecker, memberExistenceChecker);
    given(hubFinder.find(any())).willReturn(hub);

    MvcTestResult result =
        mvcTester.get().uri("/v1/hubs/{hubId}", hub.getId().toString()).exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.hubId", isEqualTo(hub.getId().toString()));
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void update() throws JsonProcessingException {
    HubUpdateRequest request = HubFixture.createUpdateRequest();

    Hub hub = HubFixture.create(permissionChecker, memberExistenceChecker);
    given(hubManager.updateInfo(any(), any(HubUpdateRequest.class), any())).willReturn(hub);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .put()
            .uri("/v1/hubs/{hubId}", hub.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.hubId", isEqualTo(hub.getId().toString()));
  }

  @Test
  @MockUser(roles = "SHIPPING_AGENT")
  void updateIfUnauthorized() throws JsonProcessingException {
    HubUpdateRequest request = HubFixture.createUpdateRequest();

    Hub hub = HubFixture.create(permissionChecker, memberExistenceChecker);
    given(hubManager.updateInfo(any(), any(HubUpdateRequest.class), any())).willReturn(hub);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .put()
            .uri("/v1/hubs/{hubId}", hub.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void delete() {
    Hub hub = HubFixture.create(permissionChecker, memberExistenceChecker);
    given(hubManager.delete(any(), anyString(), any())).willReturn(hub);

    MvcTestResult result =
        mvcTester.delete().uri("/v1/hubs/{hubId}", hub.getId().toString()).exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.hubId", isEqualTo(hub.getId().toString()));
  }

  @Test
  @MockUser(roles = "SHIPPING_AGENT")
  void deleteIfUnauthorized() {
    Hub hub = HubFixture.create(permissionChecker, memberExistenceChecker);
    given(hubManager.delete(any(), anyString(), any())).willReturn(hub);

    MvcTestResult result =
        mvcTester.delete().uri("/v1/hubs/{hubId}", hub.getId().toString()).exchange();

    assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void findManager() {
    HubManagerInfo manager = HubFixture.getManager(hub.getManagerId());
    given(hubFinder.findManager(any(UUID.class))).willReturn(manager);

    MvcTestResult result =
        mvcTester.get().uri("/v1/hubs/{hubId}/manager", hub.getId().toString()).exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.id", isEqualTo(manager.id().toString()))
        .hasPathSatisfying("$.name", isEqualTo(manager.name()))
        .hasPathSatisfying("$.username", isEqualTo(manager.username()))
        .hasPathSatisfying("$.slackId", isEqualTo(manager.slackId()));
  }

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void changeManager() throws JsonProcessingException {
    HubManagerChangeRequest request = new HubManagerChangeRequest(UUID.randomUUID());
    doNothing().when(hubManager).changeManager(any(UUID.class), any(UUID.class), any(UUID.class));
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .put()
            .uri("/v1/hubs/{hubId}/manager", hub.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result).hasStatusOk();
  }

  @Test
  @MockUser(roles = "SHIPPING_AGENT")
  void changeManagerIfUnauthorized() throws JsonProcessingException {
    HubManagerChangeRequest request = new HubManagerChangeRequest(UUID.randomUUID());
    doNothing().when(hubManager).changeManager(any(UUID.class), any(UUID.class), any(UUID.class));
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .put()
            .uri("/v1/hubs/{hubId}/manager", hub.getId().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
  }
}
