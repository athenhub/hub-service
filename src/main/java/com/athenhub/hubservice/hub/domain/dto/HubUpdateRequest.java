package com.athenhub.hubservice.hub.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 허브 정보 수정을 위한 요청 DTO.
 *
 * <p>기존 {@code Hub} 엔티티의 정보를 변경하기 위해 클라이언트로부터 전달되는 데이터 구조이다. 필드별로 {@code @NotNull} 검증 애너테이션이 적용되어
 * 있으며, 이는 수정 요청 시 반드시 포함되어야 하는 필드를 의미한다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>{@code name} — 허브명 (필수)
 *   <li>{@code streetAddress} — 기본 주소 (필수)
 *   <li>{@code detailAddress} — 상세 주소 (선택)
 *   <li>{@code latitude} — 위도 (필수)
 *   <li>{@code longitude} — 경도 (필수)
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record HubUpdateRequest(
    @NotBlank String name,
    @NotBlank String streetAddress,
    String detailAddress,
    @NotNull Double latitude,
    @NotNull Double longitude) {}
