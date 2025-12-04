package jp.asatex.ggsz.socialinsurance.application;

import jp.asatex.ggsz.socialinsurance.application.dto.SocialInsuranceApplicationDto;
import reactor.core.publisher.Mono;

/**
 * 应用层社会保险服务接口
 */
public interface SocialInsuranceApplicationService {
    
    /**
     * 查询社会保险金额
     *
     * @param monthlySalary 月薪
     * @param age 年龄
     * @return 包含社会保险金额的DTO
     */
    Mono<SocialInsuranceApplicationDto> socialInsuranceQuery(Integer monthlySalary, Integer age);
}
