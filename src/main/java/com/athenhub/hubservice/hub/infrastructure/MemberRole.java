package com.athenhub.hubservice.hub.infrastructure;

/**
 * 회원(Member)의 역할을 나타내는 열거형.
 *
 * <p>각 역할에 따라 권한과 접근 범위가 결정된다.
 *
 * <ul>
 *   <li>{@link #MASTER_MANAGER} — 마스터 관리자
 *   <li>{@link #HUB_MANAGER} — 허브 관리자
 *   <li>{@link #SHIPPING_AGENT} — 배송 담당자
 *   <li>{@link #VENDOR_AGENT} — 업체 담당자
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public enum MemberRole {
  MASTER_MANAGER,
  HUB_MANAGER,
  SHIPPING_AGENT,
  VENDOR_AGENT
}
