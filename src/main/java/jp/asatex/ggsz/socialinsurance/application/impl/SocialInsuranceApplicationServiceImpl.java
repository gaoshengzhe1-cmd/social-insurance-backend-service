package jp.asatex.ggsz.socialinsurance.application.impl;

import jp.asatex.ggsz.socialinsurance.application.SocialInsuranceApplicationService;
import jp.asatex.ggsz.socialinsurance.application.dto.SocialInsuranceApplicationDto;
import jp.asatex.ggsz.socialinsurance.dto.SocialInsuranceDomainDto;
import jp.asatex.ggsz.socialinsurance.service.SocialInsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 应用层社会保险服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialInsuranceApplicationServiceImpl implements SocialInsuranceApplicationService {

    private final SocialInsuranceService socialInsuranceService;

    @Override
    public Mono<SocialInsuranceApplicationDto> socialInsuranceQuery(Integer monthlySalary, Integer age) {
        // 参数校验
        if (monthlySalary == null || monthlySalary <= 0) {
            return Mono.error(new IllegalArgumentException("月薪必须大于0"));
        }
        if (age == null || age <= 0) {
            return Mono.error(new IllegalArgumentException("年龄必须大于0"));
        }

        log.debug("查询社会保险金额: monthlySalary={}, age={}", monthlySalary, age);
        
        // 调用领域服务获取数据
        return socialInsuranceService.socialInsuranceQuery(monthlySalary, age)
                .map(this::convertToApplicationDto)
                .doOnSuccess(dto -> log.debug("社会保险金额查询成功: {}", dto))
                .doOnError(e -> log.error("社会保险金额查询失败: {}", e.getMessage(), e));
    }

    /**
     * 将领域层DTO转换为应用层DTO
     */
    private SocialInsuranceApplicationDto convertToApplicationDto(SocialInsuranceDomainDto domainDto) {
        SocialInsuranceDomainDto.CostDetail domainEmployeeCost = domainDto.getEmployeeCost();
        SocialInsuranceDomainDto.CostDetail domainEmployerCost = domainDto.getEmployerCost();

        SocialInsuranceApplicationDto.CostDetail employeeCost = domainEmployeeCost != null ? 
            SocialInsuranceApplicationDto.CostDetail.builder()
                .healthCostWithNoCare(domainEmployeeCost.getHealthCostWithNoCare())
                .careCost(domainEmployeeCost.getCareCost())
                .pension(domainEmployeeCost.getPension())
                .employmentInsurance(domainEmployeeCost.getEmploymentInsurance())
                .incomeTax(domainEmployeeCost.getIncomeTax())
                .build() : null;

        SocialInsuranceApplicationDto.CostDetail employerCost = domainEmployerCost != null ?
            SocialInsuranceApplicationDto.CostDetail.builder()
                .healthCostWithNoCare(domainEmployerCost.getHealthCostWithNoCare())
                .careCost(domainEmployerCost.getCareCost())
                .pension(domainEmployerCost.getPension())
                .employmentInsurance(domainEmployerCost.getEmploymentInsurance())
                .incomeTax(null) // 雇主没有所得税
                .build() : null;

        return SocialInsuranceApplicationDto.builder()
                .employeeCost(employeeCost)
                .employerCost(employerCost)
                .build();
    }
}
