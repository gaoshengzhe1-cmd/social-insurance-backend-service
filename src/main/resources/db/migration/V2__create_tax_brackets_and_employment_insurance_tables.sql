-- ===========================================
-- 创建税率表和雇佣保险费率表并插入数据
-- 用于源泉税和雇佣保险的计算
-- ===========================================

-- 创建税率表
CREATE TABLE tax_brackets (
    id BIGSERIAL PRIMARY KEY,
    min_income INTEGER NOT NULL,
    max_income INTEGER,
    base_tax_0 INTEGER NOT NULL,
    base_tax_1 INTEGER NOT NULL,
    base_tax_2 INTEGER NOT NULL,
    base_tax_3 INTEGER NOT NULL,
    base_tax_4 INTEGER NOT NULL,
    base_tax_5 INTEGER NOT NULL,
    base_tax_6 INTEGER NOT NULL,
    base_tax_7 INTEGER NOT NULL,
    tax_rate DECIMAL(10, 6),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加表注释
COMMENT ON TABLE tax_brackets IS '源泉税税率表，用于计算所得税';
COMMENT ON COLUMN tax_brackets.min_income IS '最小收入';
COMMENT ON COLUMN tax_brackets.max_income IS '最大收入（NULL表示无上限）';
COMMENT ON COLUMN tax_brackets.base_tax_0 IS '0个抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.base_tax_1 IS '1个抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.base_tax_2 IS '2个抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.base_tax_3 IS '3个抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.base_tax_4 IS '4个抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.base_tax_5 IS '5个抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.base_tax_6 IS '6个抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.base_tax_7 IS '7个及以上抚养人的基础税额';
COMMENT ON COLUMN tax_brackets.tax_rate IS '税率';
COMMENT ON COLUMN tax_brackets.is_active IS '是否启用';

-- 创建索引
CREATE INDEX idx_tax_brackets_income_range ON tax_brackets(min_income, max_income);
CREATE INDEX idx_tax_brackets_active ON tax_brackets(is_active);

-- 插入完整的源泉税税率表数据
-- 插入740,000円以下的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(0, 740000, 23520, 20130, 16740, 13350, 9960, 6570, 3180, 0, 0.0000, true);

-- 插入740,000円 ~ 780,000円的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(740000, 780000, 73390, 66920, 60450, 53980, 47520, 41050, 34580, 28120, 0.2042, true);

-- 插入780,000円 ~ 950,000円的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(780000, 950000, 81560, 75090, 68620, 62150, 55690, 49220, 42750, 36290, 0.23483, true);

-- 插入950,000円 ~ 1,700,000円的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(950000, 1700000, 121480, 115010, 108540, 102070, 95610, 89140, 82670, 76210, 0.33693, true);

-- 创建雇佣保险费率表
CREATE TABLE employment_insurance_rates (
    id BIGSERIAL PRIMARY KEY,
    employment_type VARCHAR(20) NOT NULL,
    rate DECIMAL(10, 4) NOT NULL,
    effective_date VARCHAR(10) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加表注释
COMMENT ON TABLE employment_insurance_rates IS '雇佣保险费率表';
COMMENT ON COLUMN employment_insurance_rates.employment_type IS '雇佣类型（GENERAL/AGRICULTURE/CONSTRUCTION）';
COMMENT ON COLUMN employment_insurance_rates.rate IS '费率';
COMMENT ON COLUMN employment_insurance_rates.effective_date IS '生效日期（YYYY-MM-DD格式）';
COMMENT ON COLUMN employment_insurance_rates.is_active IS '是否启用';

-- 创建索引
CREATE INDEX idx_employment_insurance_rates_type ON employment_insurance_rates(employment_type);
CREATE INDEX idx_employment_insurance_rates_active ON employment_insurance_rates(is_active);
CREATE UNIQUE INDEX idx_employment_insurance_rates_unique ON employment_insurance_rates(employment_type, is_active) WHERE is_active = TRUE;

-- 插入雇佣保险费率数据
INSERT INTO employment_insurance_rates (employment_type, rate, effective_date, is_active) VALUES
('GENERAL', 0.0055, '2025-01-01', true),
('AGRICULTURE', 0.0055, '2025-01-01', true),
('CONSTRUCTION', 0.0055, '2025-01-01', true);
