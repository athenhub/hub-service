package com.athenhub.hubservice.hub.application.service;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.dto.HubSearchCondition;
import com.athenhub.hubservice.hub.domain.vo.HubManagerInfo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 허브(Hub) 조회를 담당하는 도메인 서비스 인터페이스.
 *
 * <p>허브 식별자 {@code HubId}를 기반으로 등록된 허브 엔티티를 조회하며, 조회 실패 시 도메인 규칙에 따라 예외를 발생시킬 수 있다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>Hub 단건 조회
 *   <li>존재하지 않는 경우 도메인 예외 처리
 * </ul>
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface HubFinder {
  /**
   * 허브를 단건 조회한다.
   *
   * @param hubId 조회할 허브 식별자
   * @return 조회된 {@link Hub} 엔티티
   */
  Hub find(UUID hubId);

  /**
   * 활성 상태의 허브를 모두 조회한다.
   *
   * @return 조회된 {@link Hub} 목록
   */
  List<Hub> findAllActive();

  /**
   * 전달받은 {@link HubSearchCondition} 기반으로 허브를 검색한다.
   *
   * <p>검색 조건 객체의 각 필드는 모두 선택적(optional)이며, null 또는 기본값일 경우 해당 필터는 검색에 적용되지 않는다. 검색 조건은 허브 타입, 허브
   * ID, 키워드 기반 부분 검색, 삭제 여부 포함 여부 등이 포함된다.
   *
   * <p>페이징 처리는 {@link Pageable} 의 설정을 따른다.
   *
   * @param searchCondition 허브 검색 조건 객체
   * @return 조건에 맞는 {@link Hub} 목록을 포함하는 페이지 결과
   */
  Page<Hub> search(HubSearchCondition searchCondition, Pageable pageable);

  /**
   * 허브의 관리자 정보를 조회한다.
   *
   * @param hubId 조회할 허브
   * @return HubId에 해당하는 허브의 관리자 정보
   */
  HubManagerInfo findManager(UUID hubId);
}
