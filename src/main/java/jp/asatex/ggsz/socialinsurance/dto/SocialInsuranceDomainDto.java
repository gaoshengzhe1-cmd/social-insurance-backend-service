package jp.asatex.ggsz.socialinsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 社会保险金额查询结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialInsuranceDomainDto {

    /**
     * 雇员承担的费用
     */
    private CostDetail employeeCost;

    /**
     * 雇主承担的费用
     */
    private CostDetail employerCost;

    /**
     * 费用明细
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostDetail {
        /**
         * 无介护健康保险金额
         */
        private BigDecimal healthCostWithNoCare;

        /**
         * 介护保险金额
         */
        private BigDecimal careCost;

        /**
         * 厚生年金金额
         */
        private BigDecimal pension;

        /**
         * 雇用保险金额（雇员负担）
         */
        private BigDecimal employmentInsurance;

        /**
         * 所得税金额（雇员负担）
         */
        private BigDecimal incomeTax;
    }

    /**
     * 创建包含雇员和雇主费用的SocialInsuranceDomainDto
     * @param totalHealthCost 健康保险总费用
     * @param totalCareCost 介护保险总费用
     * @param totalPension 厚生年金总费用
     * @return 包含分摊后费用的SocialInsuranceDomainDto
     */
    public static SocialInsuranceDomainDto createWithSharedCosts(
            BigDecimal totalHealthCost,
            BigDecimal totalCareCost,
            BigDecimal totalPension) {

        // 计算各项费用的50%
        BigDecimal half = new BigDecimal("0.5");

        CostDetail employee = CostDetail.builder()
                .healthCostWithNoCare(calculateHalf(totalHealthCost, half))
                .careCost(calculateHalf(totalCareCost, half))
                .pension(calculateHalf(totalPension, half))
                .build();

        CostDetail employer = CostDetail.builder()
                .healthCostWithNoCare(calculateHalf(totalHealthCost, half))
                .careCost(calculateHalf(totalCareCost, half))
                .pension(calculateHalf(totalPension, half))
                .build();

        return SocialInsuranceDomainDto.builder()
                .employeeCost(employee)
                .employerCost(employer)
                .build();
    }

    private static BigDecimal calculateHalf(BigDecimal amount, BigDecimal half) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(half).setScale(0, RoundingMode.HALF_UP);
    }
}
