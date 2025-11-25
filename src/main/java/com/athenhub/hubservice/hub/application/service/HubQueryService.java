package com.athenhub.hubservice.hub.application.service;

import com.athenhub.hubservice.hub.domain.Hub;
import com.athenhub.hubservice.hub.domain.HubRepository;
import com.athenhub.hubservice.hub.domain.dto.HubSearchCondition;
import com.athenhub.hubservice.hub.domain.service.HubManagerInfoFinder;
import com.athenhub.hubservice.hub.domain.vo.HubId;
import com.athenhub.hubservice.hub.domain.vo.HubManagerInfo;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 허브(Hub) 조회 기능을 제공하는 서비스 구현체.
 *
 * <p>해당 서비스는 {@link HubFinder} 인터페이스를 구현하며, 허브 조회와 관련된 읽기(Read) 작업만 담당한다. 조회 전용 서비스이므로 전체 트랜잭션은
 * {@code readOnly = true} 설정이 적용된다.
 *
 * <h2>역할</h2>
 *
 * <ul>
 *   <li>허브 단건 조회
 *   <li>허브 검색
 *   <li>조회 시 필요한 부가 검증 또는 예외 처리 수행
 * </ul>
 *
 * <p>조회 기능은 비즈니스 로직이 아닌 읽기 책임에 집중하도록 분리되어 있으며, 생성자 기반 의존성 주입은 {@link RequiredArgsConstructor}에 의해
 * 처리된다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HubQueryService implements HubFinder {

  private final HubRepository hubRepository;
  private final HubManagerInfoFinder hubManagerInfoFinder;

  @Override
  public Hub find(UUID hubId) {
    return hubRepository
        .findById(HubId.of(hubId))
        .orElseThrow(
            () -> new IllegalArgumentException("허브 정보를 찾을수 없습니다. id: " + hubId.toString()));
  }

  @Override
  public List<Hub> findAllActive() {
    return hubRepository.findAllByDeletedAtIsNull();
  }

  @Override
  public Page<Hub> search(HubSearchCondition searchCondition, Pageable pageable) {
    String keyword =
        Objects.isNull(searchCondition.keyword()) ? null : searchCondition.keyword().toUpperCase();

    return hubRepository.search(keyword, searchCondition.includeDeleted(), pageable);
  }

  @Override
  public HubManagerInfo findManager(UUID hubId) {
    Hub hub = find(hubId);

    return hub.getManagerInfo(hubManagerInfoFinder);
  }
}
