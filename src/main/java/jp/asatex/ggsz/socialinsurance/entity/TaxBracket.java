package jp.asatex.ggsz.socialinsurance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("tax_brackets")
public class TaxBracket {
    @Id
    private Long id;
    
    @Column("min_income")
    private Integer minIncome;
    
    @Column("max_income")
    private Integer maxIncome; // null means no upper limit
    
    @Column("base_tax_0")
    private Integer baseTax0; // Base tax for 0 dependents
    
    @Column("base_tax_1")
    private Integer baseTax1; // Base tax for 1 dependent
    
    @Column("base_tax_2")
    private Integer baseTax2; // Base tax for 2 dependents
    
    @Column("base_tax_3")
    private Integer baseTax3; // Base tax for 3 dependents
    
    @Column("base_tax_4")
    private Integer baseTax4; // Base tax for 4 dependents
    
    @Column("base_tax_5")
    private Integer baseTax5; // Base tax for 5 dependents
    
    @Column("base_tax_6")
    private Integer baseTax6; // Base tax for 6 dependents
    
    @Column("base_tax_7")
    private Integer baseTax7; // Base tax for 7+ dependents
    
    @Column("tax_rate")
    private BigDecimal taxRate;
    
    @Column("is_active")
    private Boolean active = true;
}
