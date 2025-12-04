package jp.asatex.ggsz.socialinsurance.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * API层社会保险金额查询结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialInsuranceDto {
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
     * 雇用保险金额
     */
    private BigDecimal employmentInsurance;
    
    /**
     * 所得税金额
     */
    private BigDecimal incomeTax;
}
