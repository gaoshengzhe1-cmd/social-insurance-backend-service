package jp.asatex.ggsz.pensioninsurance.service.impl;

import jp.asatex.ggsz.pensioninsurance.dto.PensionInsuranceDto;
import jp.asatex.ggsz.pensioninsurance.entity.PensionBracket;
import jp.asatex.ggsz.pensioninsurance.repository.PensionBracketRepository;
import jp.asatex.ggsz.pensioninsurance.service.PensionInsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class PensionInsuranceServiceImpl implements PensionInsuranceService {

    private final PensionBracketRepository pensionBracketRepository;

    @Override
    public Mono<PensionInsuranceDto> calculatePensionInsurance(Integer monthlySalary) {
        return pensionBracketRepository.findBracketByAmount(monthlySalary)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No matching pension bracket found for the given salary")))
                .map(bracket -> {
                    // 计算厚生年金（个人负担50%）
                    BigDecimal employeePension = bracket.getPension()
                            .divide(new BigDecimal("2"), 0, RoundingMode.HALF_UP);
                    
                    // 雇主负担：厚生年金（个人负担50%）
                    BigDecimal employerPension = bracket.getPension()
                            .divide(new BigDecimal("2"), 0, RoundingMode.HALF_UP);

                    // 创建DTO并设置各项费用
                    return PensionInsuranceDto.builder()
                            .employeeCost(PensionInsuranceDto.CostDetail.builder()
                                    .pension(employeePension)
                                    .build())
                            .employerCost(PensionInsuranceDto.CostDetail.builder()
                                    .pension(employerPension)
                                    .build())
                            .build();
                });
    }
}
