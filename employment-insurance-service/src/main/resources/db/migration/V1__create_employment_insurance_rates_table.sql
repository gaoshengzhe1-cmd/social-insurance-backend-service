-- ===========================================
-- 创建雇佣保险费率表并插入数据
-- 用于雇佣保险的计算
-- ===========================================

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

-- 创建触发器函数，用于自动更新 updated_at 字段
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 创建触发器
CREATE TRIGGER update_employment_insurance_rates_updated_at
    BEFORE UPDATE ON employment_insurance_rates
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
