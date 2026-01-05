package jp.asatex.ggsz.pensioninsurance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("pension_bracket")
public class PensionBracket {
    @Id
    private Long id;
    
    @Column("grade")
    private String grade;
    
    @Column("std_rem")
    private Integer stdRem;
    
    @Column("min_amount")
    private Integer minAmount;
    
    @Column("max_amount")
    private Integer maxAmount;
    
    @Column("pension")
    private BigDecimal pension;
    
    @Column("created_at")
    private LocalDateTime createdAt;
    
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
