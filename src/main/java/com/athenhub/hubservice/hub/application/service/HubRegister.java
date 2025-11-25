package com.athenhub.hubservice.hub.application.service;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubRegisterRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 신규 허브(Hub) 등록을 수행하는 도메인 서비스 인터페이스.
 *
 * <p>허브 생성에 필요한 요청 정보를 {@link HubRegisterRequest} 형태로 전달받아 내부 도메인 정책에 따라 {@link Hub} 엔티티를 생성하고
 * 저장한다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>허브 등록 요청의 유효성 검사(Bean Validation 기반)
 *   <li>Hub 엔티티 생성 및 초기 상태 설정
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface HubRegister {
  /**
   * 신규 허브를 등록한다.
   *
   * @param registerRequest 등록 요청 DTO (필수, 유효성 검사 적용)
   * @param requestId 요청자 ID (필수)
   * @return 생성된 {@link Hub} 엔티티
   */
  Hub register(@Valid HubRegisterRequest registerRequest, @NotNull UUID requestId);
}
