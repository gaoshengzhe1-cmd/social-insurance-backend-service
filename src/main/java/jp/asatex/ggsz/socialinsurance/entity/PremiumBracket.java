package jp.asatex.ggsz.socialinsurance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 社会保险费等级实体类
 * 对应数据库表 premium_bracket
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("premium_bracket")
public class PremiumBracket {

    /**
     * 主键ID
     */
    @Id
    private Long id;

    /**
     * 等级
     */
    private String grade;

    /**
     * 标准报酬
     */
    @Column("std_rem")
    private Integer stdRem;

    /**
     * 最小值
     */
    @Column("min_amount")
    private Integer minAmount;

    /**
     * 最大值（999999999表示无上限）
     */
    @Column("max_amount")
    private Integer maxAmount;

    /**
     * 健康保险费（无护理）
     */
    @Column("health_no_care")
    private BigDecimal healthNoCare;

    /**
     * 健康保险费（有护理）
     */
    @Column("health_care")
    private BigDecimal healthCare;

    /**
     * 厚生年金保险费（0表示不适用）
     */
    private BigDecimal pension;

    /**
     * 创建时间
     */
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
