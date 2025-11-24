package com.athenhub.hubservice.hub.infrastructure;

/**
 * 회원(Member)의 상태을 나타내는 열거형.
 *
 * <ul>
 *   <li>{@link #PENDING} — 승인 대기 상태
 *   <li>{@link #ACTIVATED} — 활성 상태
 *   <li>{@link #REJECTED} — 승인 거절 상태
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public enum MemberStatus {
  PENDING,
  ACTIVATED,
  REJECTED
}
