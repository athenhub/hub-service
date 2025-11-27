package com.athenhub.hubservice.hub.webapi.dto;

import com.athenhub.hubservice.hub.domain.Hub;
import java.util.Objects;
import java.util.UUID;

/**
 * 허브 조회 응답 DTO.
 *
 * <p>특정 허브를 조회할 때 반환되는 응답 모델로, 허브의 기본 정보와 주소/좌표 정보를 포함한다. 본 레코드는 읽기 전용 구조이며, 컨트롤러 계층에서 API 응답 변환을
 * 위해 사용된다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>hubId — 허브 식별자(UUID)
 *   <li>name — 허브명
 *   <li>streetAddress — 주소
 *   <li>addressDetail — 상세 주소
 *   <li>latitude — 위도
 *   <li>longitude — 경도
 *   <li>isDeleted - 삭제 여부
 * </ul>
 *
 * <p>정적 메서드 {@link #from(Hub)}를 통해 도메인 엔티티를 쉽게 응답 DTO로 변환할 수 있다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record HubFindResponse(
    UUID hubId,
    String name,
    String streetAddress,
    String detailAddress,
    Double latitude,
    Double longitude,
    boolean isDeleted) {

  /**
   * 도메인 엔티티 {@link Hub}로부터 조회 응답 객체를 생성한다.
   *
   * @param hub 조회된 허브 도메인 객체
   * @return {@link HubFindResponse} 변환 결과
   */
  public static HubFindResponse from(Hub hub) {
    return new HubFindResponse(
        hub.getId().toUuid(),
        hub.getName(),
        hub.getAddress().getStreet(),
        hub.getAddress().getDetail(),
        hub.getCoordinate().getLatitude(),
        hub.getCoordinate().getLongitude(),
        !Objects.isNull(hub.getDeletedAt()));
  }
}
