package com.athenhub.hubservice.hub.domain.dto;

/**
 * 허브 검색을 위한 다양한 조건을 담는 불변(immutable) 검색 조건 객체.
 *
 * <p>해당 레코드는 허브 검색 시 적용할 필터링 조건을 하나의 객체로 묶어 전달하기 위한 목적을 가진다. 모든 필드는 선택적(optional)이므로, null 또는 기본값이
 * 들어올 경우 해당 조건은 검색에 적용되지 않는다.
 *
 * <h2>검색 조건</h2>
 *
 * <ul>
 *   <li><b>keyword</b> — 이름, 주소, 상세주소 기반 부분 검색 키워드 (null 시 조건 미적용)
 *   <li><b>includeDeleted</b> — 삭제된 허브 포함 여부
 *   <li><b>pageable</b> — 페이징 및 정렬 정보
 * </ul>
 *
 * <p>해당 레코드는 도메인 서비스, 조회 서비스, 또는 Repository 계층에서 검색 조건 전달용으로 사용된다.
 *
 * @param keyword 부분 일치 검색에 사용할 키워드
 * @param includeDeleted true일 경우 삭제된 허브도 결과에 포함
 * @author 김형섭
 * @since 1.0.0
 */
public record HubSearchCondition(String keyword, boolean includeDeleted) {

  /**
   * {@link HubSearchCondition} 생성용 정적 팩토리 메서드.
   *
   * @param keyword 검색 키워드
   * @param includeDeleted 삭제된 허브 포함 여부
   * @return 새로운 {@link HubSearchCondition} 인스턴스
   */
  public static HubSearchCondition of(String keyword, boolean includeDeleted) {
    return new HubSearchCondition(keyword, includeDeleted);
  }
}
