package com.athenhub.hubservice.hub.infrastructure.client;

import com.athenhub.hubservice.hub.infrastructure.dto.NaverRouteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 네이버 지도 Directions5 API(자동차 길찾기)를 호출하는 Feign 클라이언트.
 *
 * <p>네이버 클라우드 플랫폼(NCP)의 지도 API Gateway를 통해 자동차 경로 탐색 정보를 조회한다. <br>
 * 해당 클라이언트는 API 호출 시 필요한 인증 값(X-NCP-APIGW-API-KEY-ID, X-NCP-APIGW-API-KEY) 을 {@link
 * NaverFeignConfig} 설정을 통해 자동으로 포함한다.
 *
 * <p>공식 문서: NCP Map Direction v5
 *
 * @author 김형섭
 * @since 1.0.0
 */
@FeignClient(
    name = "naver-map",
    url = "https://maps.apigw.ntruss.com/map-direction/v1",
    configuration = NaverFeignConfig.class)
public interface NaverDirections5Client {
  /**
   * 네이버 지도 Directions5 API를 호출하여 차량 이동 기준의 경로 정보를 조회한다.
   *
   * <p>시작 지점과 도착 지점은 "경도,위도" 형식의 문자열로 전달하며, 차량 종류(cartype)에 따라 경로가 다르게 계산될 수 있다.
   *
   * <ul>
   *   <li><b>start</b> – 시작 지점 좌표(예: "126.97843,37.56668")
   *   <li><b>goal</b> – 도착 지점 좌표(예: "127.02758,37.49794")
   *   <li><b>cartype</b> – 차량 유형(예: 1 = 일반 차량)
   * </ul>
   *
   * @param start 경도,위도 형식의 시작 지점 좌표
   * @param goal 경도,위도 형식의 도착 지점 좌표
   * @param cartype 차량 유형 코드
   * @return 네이버 지도 경로 탐색 결과를 담은 {@link NaverRouteResponse}
   */
  @GetMapping(value = "/driving")
  NaverRouteResponse getRoute(
      @RequestParam("start") String start,
      @RequestParam("goal") String goal,
      @RequestParam("cartype") Integer cartype);
}
