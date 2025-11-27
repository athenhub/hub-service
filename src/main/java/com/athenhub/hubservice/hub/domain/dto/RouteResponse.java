package com.athenhub.hubservice.hub.domain.dto;

/**
 * 두 지점 간 경로 정보(거리, 소요 시간)를 담는 응답 DTO.
 *
 * <p>거리와 소요 시간을 계산한 값을 사용하며, 단순한 조회 결과를 전달하기 위한 불변(immutable) 데이터 구조이다.
 *
 * @param distanceKm 이동 거리(km 단위). 경로가 없으면 {@code null}
 * @param durationMinutes 이동에 필요한 총 소요 시간(분 단위). 경로가 없으면 {@code null}
 * @author 김형섭
 * @since 1.0.0
 */
public record RouteResponse(Double distanceKm, Integer durationMinutes) {}
