package com.athenhub.hubservice.presentation.webapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.athenhub.hubservice.MockUser;
import com.athenhub.hubservice.hub.application.service.HubRouteService;
import com.athenhub.hubservice.hub.domain.HubRoute;
import com.athenhub.hubservice.hub.domain.vo.HubId;
import com.athenhub.hubservice.hub.webapi.dto.HubRouteResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@SpringBootTest
@AutoConfigureMockMvc
class HubRouteApiTest {

  @Autowired MockMvcTester mvcTester;

  @MockitoBean HubRouteService hubRouteService;

  @Autowired ObjectMapper objectMapper;

  @Test
  @MockUser(roles = "MASTER_MANAGER")
  void findAll() throws JsonProcessingException, UnsupportedEncodingException {
    HubId sourceId = HubId.of(UUID.randomUUID());
    HubRoute route = HubRoute.create(sourceId, HubId.of(UUID.randomUUID()), 36.2, 35);
    HubRoute route2 = HubRoute.create(sourceId, HubId.of(UUID.randomUUID()), 32.2, 30);
    HubRoute route3 = HubRoute.create(sourceId, HubId.of(UUID.randomUUID()), 60.2, 60);

    List<HubRoute> routes = List.of(route, route2, route3);
    given(hubRouteService.findAllSourceBy(any(UUID.class))).willReturn(routes);

    MvcTestResult result =
        mvcTester.get().uri("/v1/hubs/{hubId}/routes", sourceId.toString()).exchange();

    assertThat(result).hasStatusOk();

    List<HubRouteResponse> actualResponses =
        objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

    List<HubRouteResponse> expectedResponses = routes.stream().map(HubRouteResponse::from).toList();

    assertThat(actualResponses).containsExactlyInAnyOrderElementsOf(expectedResponses);
  }
}
