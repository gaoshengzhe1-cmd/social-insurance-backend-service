package jp.asatex.ggsz.employmentinsurance.service.impl;

import jp.asatex.ggsz.employmentinsurance.dto.EmploymentInsuranceDto;
import jp.asatex.ggsz.employmentinsurance.entity.EmploymentInsuranceRate;
import jp.asatex.ggsz.employmentinsurance.repository.EmploymentInsuranceRateRepository;
import jp.asatex.ggsz.employmentinsurance.service.EmploymentInsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmploymentInsuranceServiceImpl implements EmploymentInsuranceService {

    @Value("${employment.insurance.rounding-threshold:0.50000001}")
    private BigDecimal roundingThreshold;

    private final EmploymentInsuranceRateRepository employmentInsuranceRateRepository;

    @Override
    public Mono<EmploymentInsuranceDto> calculateEmploymentInsurance(Integer monthlySalary, String employmentType) {
        EmploymentInsuranceRate.EmploymentType type = EmploymentInsuranceRate.EmploymentType.valueOf(employmentType.toUpperCase());
        
        return employmentInsuranceRateRepository.findByEmploymentTypeAndActiveTrue(type)
                .switchIfEmpty(Mono.error(new IllegalStateException("No active employment insurance rate found for type: " + employmentType)))
                .map(rate -> {
                    // 雇员负担：雇用保险（从数据库获取费率）
                    BigDecimal employeeEmployment = roundSpecial(
                            BigDecimal.valueOf(monthlySalary).multiply(rate.getRate())
                    );

                    // 雇主负担：雇用保险（从数据库获取费率）
                    BigDecimal employerEmployment = roundSpecial(
                            BigDecimal.valueOf(monthlySalary).multiply(rate.getRate())
                    );

                    // 创建DTO并设置各项费用
                    return EmploymentInsuranceDto.builder()
                            .employeeCost(EmploymentInsuranceDto.CostDetail.builder()
                                    .employmentInsurance(employeeEmployment)
                                    .build())
                            .employerCost(EmploymentInsuranceDto.CostDetail.builder()
                                    .employmentInsurance(employerEmployment)
                                    .build())
                            .build();
                });
    }

    private BigDecimal roundSpecial(BigDecimal amount) {
        BigDecimal floor = amount.setScale(0, RoundingMode.FLOOR);
        BigDecimal fraction = amount.subtract(floor);
        if (fraction.compareTo(roundingThreshold) > 0) {
            return floor.add(BigDecimal.ONE);
        }
        return floor;
    }
}
