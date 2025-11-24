package com.athenhub.hubservice.hub.domain.exception;

import com.athenhub.commoncore.error.AbstractServiceException;
import com.athenhub.commoncore.error.ErrorCode;

/**
 * 권한 검증 실패 시 발생하는 도메인 서비스 예외 클래스.
 *
 * <p>{@link AbstractServiceException}를 상속하며, {@link ErrorCode}와 가변 인자를 통해 구체적인 에러 정보를 전달할 수 있다.
 *
 * <p>주로 {@link PermissionErrorCode}와 함께 사용되어 권한 관련 예외를 표준화한다.
 *
 * <p>예시:
 *
 * <pre>{@code
 * if (!permissionChecker.hasManagePermission(requestId)) {
 *     throw new PermissionException(PermissionErrorCode.HAS_NOT_MANAGE_PERMISSION);
 * }
 * }</pre>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public class PermissionException extends AbstractServiceException {
  /**
   * 권한 예외 생성자.
   *
   * @param errorCode 예외에 대한 {@link ErrorCode} 정보
   * @param errorArgs 에러 메시지 포맷에 사용될 가변 인자
   */
  public PermissionException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }
}
