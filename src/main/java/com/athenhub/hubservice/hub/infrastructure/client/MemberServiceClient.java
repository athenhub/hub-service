package com.athenhub.hubservice.hub.infrastructure.client;

import com.athenhub.hubservice.hub.infrastructure.dto.MemberInfo;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Member 관련 외부 서비스 호출을 위한 Feign 클라이언트.
 *
 * <p>회원 서비스(member-service)와 통신하여 특정 회원의 상세 정보를 조회한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@FeignClient("192.168.0.13:9000/member-service")
public interface MemberServiceClient {
  /**
   * 지정된 회원의 상세 정보를 조회한다.
   *
   * @param memberId 회원 식별자(UUID)
   * @return 회원 정보 객체 {@link MemberInfo}
   */
  @GetMapping("profile/{memberId}")
  MemberInfo getMemberInfo(@PathVariable("memberId") UUID memberId);
}
