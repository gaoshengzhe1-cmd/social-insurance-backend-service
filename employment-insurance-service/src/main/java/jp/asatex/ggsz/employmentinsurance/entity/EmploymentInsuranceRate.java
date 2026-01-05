package jp.asatex.ggsz.employmentinsurance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("employment_insurance_rates")
public class EmploymentInsuranceRate {
    @Id
    private Long id;
    
    @Column("employment_type")
    private EmploymentType employmentType;
    
    @Column("rate")
    private BigDecimal rate;
    
    @Column("effective_date")
    private String effectiveDate;
    
    @Column("is_active")
    private Boolean isActive;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public enum EmploymentType {
        GENERAL, AGRICULTURE, CONSTRUCTION
    }
}
