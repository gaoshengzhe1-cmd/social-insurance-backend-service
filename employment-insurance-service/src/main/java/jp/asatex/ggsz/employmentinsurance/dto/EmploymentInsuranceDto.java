package jp.asatex.ggsz.employmentinsurance.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentInsuranceDto {
    private CostDetail employeeCost;
    private CostDetail employerCost;

    @Data
    @Builder
    public static class CostDetail {
        private BigDecimal employmentInsurance;
    }
}
