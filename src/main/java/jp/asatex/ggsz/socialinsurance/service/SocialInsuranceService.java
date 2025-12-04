package jp.asatex.ggsz.socialinsurance.service;

import jp.asatex.ggsz.socialinsurance.dto.SocialInsuranceDomainDto;
import reactor.core.publisher.Mono;

/**
 * 社会保险领域服务接口
 */
public interface SocialInsuranceService {
    
    /**
     * 查询社会保险金额
     *
     * @param monthlySalary 月薪
     * @param age 年龄
     * @return 包含社会保险金额的DTO
     */
    Mono<SocialInsuranceDomainDto> socialInsuranceQuery(Integer monthlySalary, Integer age);
}
