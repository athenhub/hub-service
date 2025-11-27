package com.athenhub.hubservice.hub.webapi.dto;

import java.util.UUID;

/**
 * 허브 관리자 변경 요청 정보를 담는 DTO.
 *
 * <p>해당 요청 객체는 특정 허브(Hub)의 관리자를 다른 관리자로 교체할 때 사용되며, 교체될 새로운 관리자의 식별자(UUID)를 포함한다.
 *
 * <h2>포함 정보</h2>
 *
 * <ul>
 *   <li>{@code newMangerId} — 새로 지정할 허브 관리자의 식별자
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record HubManagerChangeRequest(UUID newMangerId) {}
