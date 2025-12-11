package jp.asatex.ggsz.socialinsurance.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Data
@Configuration
@ConfigurationProperties(prefix = "social.insurance")
public class InsuranceProperties {
    private int careInsuranceAgeThreshold = 40;
    private BigDecimal employmentRateGeneral = new BigDecimal("0.0055");
    private BigDecimal employeeContributionRatio = new BigDecimal("0.5");
    private TaxTableProperties taxTable = new TaxTableProperties();
    
    @Data
    public static class TaxTableProperties {
        private int[] incomeBrackets;
        private double[] taxRates;
        private int[] baseTaxes;
        private int dependentsCap = 7;
    }
}
