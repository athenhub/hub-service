package com.athenhub.hubservice.hub.domain.exception;

import com.athenhub.commoncore.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 권한 관련 오류 코드(Enum) 정의.
 *
 * <p>도메인 내에서 권한 검증 실패 시 사용되는 표준 에러 코드를 정의하며, {@link ErrorCode} 인터페이스를 구현한다.
 *
 * <p>각 상수는 HTTP 상태 코드와 고유 코드 문자열을 포함한다.
 *
 * <ul>
 *   <li>{@link #HAS_NOT_MANAGE_PERMISSION} — 허브 관리 권한이 없는 경우
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum PermissionErrorCode implements ErrorCode {
  HAS_NOT_MANAGE_PERMISSION(HttpStatus.FORBIDDEN.value(), "HAS_NOT_MANAGE_PERMISSION");

  private final int status;
  private final String code;

  @Override
  public int getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }
}
