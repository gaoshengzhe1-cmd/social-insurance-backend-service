package jp.asatex.ggsz.pensioninsurance.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PensionInsuranceDto {
    private CostDetail employeeCost;
    private CostDetail employerCost;

    @Data
    @Builder
    public static class CostDetail {
        private BigDecimal pension;
    }
}
