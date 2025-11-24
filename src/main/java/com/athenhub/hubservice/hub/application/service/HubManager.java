package com.athenhub.hubservice.hub.application.service;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 허브(Hub)에 대한 정보 변경 및 삭제를 담당하는 도메인 서비스 인터페이스.
 *
 * <p>허브 정보 수정, 삭제 등 상태 변경 작업을 수행하며, 각 작업은 도메인 규칙 및 유효성 검증을 기반으로 처리된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>허브 정보 수정(주소, 타입, 명칭 등)
 *   <li>허브 삭제 및 삭제자 정보 기록
 *   <li>식별자 및 요청 데이터에 대한 유효성 검사
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface HubManager {
  /**
   * 허브 정보를 수정한다.
   *
   * @param hubId 수정할 허브 ID (필수)
   * @param updateRequest 수정 요청 DTO (필수, 유효성 검사 적용)
   * @return 수정된 {@link Hub} 엔티티
   */
  Hub updateInfo(
      @NotNull UUID hubId, @Valid HubUpdateRequest updateRequest, @NotNull UUID requestId);

  /**
   * 허브를 삭제한다.
   *
   * @param hubId 삭제할 허브 ID (필수)
   * @param deleteBy 삭제 요청자 (필수)
   * @return 삭제 처리된 {@link Hub} 엔티티
   */
  Hub delete(@NotNull UUID hubId, @NotBlank String deleteBy, @NotNull UUID requestId);

  /**
   * 허브 관리자를 변경한다.
   *
   * @param hubId 변경할 허브 ID (필수)
   * @param newManagerId 변경할 허브 관리자 ID (필수)
   * @param requestId 변경 요청자 (필수)
   */
  void changeManager(@NotNull UUID hubId, @NotNull UUID newManagerId, @NotNull UUID requestId);
}
