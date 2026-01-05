package jp.asatex.ggsz.socialinsurance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("employment_insurance_rates")
public class EmploymentInsuranceRate {
    
    public enum EmploymentType {
        GENERAL,        // 一般の事業
        AGRICULTURE,    // 農林水産・清酒製造の事業
        CONSTRUCTION    // 建設の事業
    }
    
    @Id
    private Long id;
    
    @Column("employment_type")
    private EmploymentType employmentType;
    
    @Column("rate")
    private BigDecimal rate;
    
    @Column("effective_date")
    private String effectiveDate; // Format: YYYY-MM-DD
    
    @Column("is_active")
    private Boolean active = true;
}
