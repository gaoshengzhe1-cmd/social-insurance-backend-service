package jp.asatex.ggsz.socialinsurance.service.impl;

import jp.asatex.ggsz.socialinsurance.dto.SocialInsuranceDomainDto;
import jp.asatex.ggsz.socialinsurance.entity.EmploymentInsuranceRate;
import jp.asatex.ggsz.socialinsurance.entity.PremiumBracket;
import jp.asatex.ggsz.socialinsurance.entity.TaxBracket;
import jp.asatex.ggsz.socialinsurance.repository.EmploymentInsuranceRateRepository;
import jp.asatex.ggsz.socialinsurance.repository.PremiumBracketRepository;
import jp.asatex.ggsz.socialinsurance.repository.TaxBracketRepository;
import jp.asatex.ggsz.socialinsurance.service.SocialInsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 社会保险领域服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialInsuranceServiceImpl implements SocialInsuranceService {

    @Value("${social.insurance.care-insurance-age-threshold:40}")
    private int careInsuranceAgeThreshold;

    @Value("${social.insurance.dependents-cap:7}")
    private int dependentsCap;

    @Value("${social.insurance.employee-contribution-ratio:2}")
    private BigDecimal employeeContributionRatio;

    @Value("${social.insurance.rounding-threshold:0.50000001}")
    private BigDecimal roundingThreshold;
    private final PremiumBracketRepository premiumBracketRepository;
    private final EmploymentInsuranceRateRepository employmentInsuranceRateRepository;
    private final TaxBracketRepository taxBracketRepository;


    @Override
    public Mono<SocialInsuranceDomainDto> socialInsuranceQuery(Integer monthlySalary, Integer age) {
        return premiumBracketRepository.findBracketByAmount(monthlySalary)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No matching premium bracket found for the given salary")))
                .flatMap(bracket -> calculateInsuranceCosts(bracket, age, monthlySalary));
    }

    private Mono<SocialInsuranceDomainDto> calculateInsuranceCosts(PremiumBracket bracket, Integer age, Integer monthlySalary) {
        return employmentInsuranceRateRepository.findByEmploymentTypeAndActiveTrue(EmploymentInsuranceRate.EmploymentType.GENERAL)
                .switchIfEmpty(Mono.error(new IllegalStateException("No active employment insurance rate found")))
                .flatMap(rate -> {
                    // 计算健康保险（个人负担50%）
                    BigDecimal employeeHealth = bracket.getHealthNoCare()
                            .divide(new BigDecimal("2"), 0, RoundingMode.HALF_UP);
                    
                    // 计算厚生年金（个人负担50%）
                    BigDecimal employeePension = bracket.getPension()
                            .divide(new BigDecimal("2"), 0, RoundingMode.HALF_UP);
                    
                    // 计算介护保险（如果需要，个人负担50%）
                    final BigDecimal employeeCare;
                    boolean needsCareInsurance = age >= careInsuranceAgeThreshold;
                    if (needsCareInsurance) {
                        employeeCare = bracket.getHealthCare()
                                .subtract(bracket.getHealthNoCare())
                                .divide(new BigDecimal("2"), 0, RoundingMode.HALF_UP);
                    } else {
                        employeeCare = BigDecimal.ZERO;
                    }

                    // 雇员负担：雇用保险（从数据库获取费率）
                    BigDecimal employeeEmployment = roundSpecial(
                            BigDecimal.valueOf(monthlySalary).multiply(rate.getRate())
                    );

                    BigDecimal socialInsuranceTotal = employeeHealth
                            .add(employeePension)
                            .add(employeeEmployment);

                    BigDecimal taxableIncomeForTax = BigDecimal.valueOf(monthlySalary)
                            .subtract(socialInsuranceTotal);
                    if (taxableIncomeForTax.compareTo(BigDecimal.ZERO) < 0) {
                        taxableIncomeForTax = BigDecimal.ZERO;
                    }

                    return calculateTaxFromDatabase(taxableIncomeForTax.intValue(), 0)
                            .map(incomeTaxAmountInt -> {
                                BigDecimal incomeTaxAmount = BigDecimal.valueOf(incomeTaxAmountInt);
                                
                                // 创建DTO并设置各项费用
                                SocialInsuranceDomainDto dto = new SocialInsuranceDomainDto();
                                
                                // 设置员工负担部分
                                SocialInsuranceDomainDto.CostDetail employeeCost = new SocialInsuranceDomainDto.CostDetail();
                                employeeCost.setHealthCostWithNoCare(employeeHealth);
                                employeeCost.setCareCost(employeeCare);
                                employeeCost.setPension(employeePension);
                                employeeCost.setEmploymentInsurance(employeeEmployment);
                                employeeCost.setIncomeTax(incomeTaxAmount);
                                
                                // 设置雇主负担部分（健康保险、介护保险、厚生年金各负担50%）
                                SocialInsuranceDomainDto.CostDetail employerCost = new SocialInsuranceDomainDto.CostDetail();
                                employerCost.setHealthCostWithNoCare(employeeHealth);
                                employerCost.setCareCost(employeeCare);
                                employerCost.setPension(employeePension);
                                
                                dto.setEmployeeCost(employeeCost);
                                dto.setEmployerCost(employerCost);
                                return dto;
                            });
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

    private Mono<Integer> calculateTaxFromDatabase(int taxableIncome, int dependents) {
        int cappedDependents = Math.min(dependents, dependentsCap);
        
        return taxBracketRepository.findBracketByIncome(taxableIncome)
                .switchIfEmpty(Mono.error(new IllegalStateException("No tax bracket found for taxable income: " + taxableIncome)))
                .map(taxBracket -> {
                    // 获取对应抚养人数的基础税额
                    int baseTax = getBaseTaxByDependents(taxBracket, cappedDependents);
                    
                    // 如果税率为0，直接返回基础税额
                    if (taxBracket.getTaxRate().compareTo(BigDecimal.ZERO) == 0) {
                        return baseTax;
                    }
                    
                    // 计算超出基础收入部分的税额
                    int excess = taxableIncome - taxBracket.getMinIncome();
                    int additionalTax = (int) Math.floor(excess * taxBracket.getTaxRate().doubleValue());
                    
                    return Math.max(0, baseTax + additionalTax);
                })
                .doOnError(e -> log.error("Error calculating tax from database: {}", e.getMessage()));
    }
    
    private int getBaseTaxByDependents(TaxBracket taxBracket, int dependents) {
        switch (dependents) {
            case 0: return taxBracket.getBaseTax0();
            case 1: return taxBracket.getBaseTax1();
            case 2: return taxBracket.getBaseTax2();
            case 3: return taxBracket.getBaseTax3();
            case 4: return taxBracket.getBaseTax4();
            case 5: return taxBracket.getBaseTax5();
            case 6: return taxBracket.getBaseTax6();
            case 7: 
            default: return taxBracket.getBaseTax7();
        }
    }
 
 }
