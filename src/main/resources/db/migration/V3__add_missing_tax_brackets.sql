-- ===========================================
-- 添加缺失的税率区间数据
-- 补充完整的源泉税税率表
-- ===========================================

-- 插入1,700,000円 ~ 2,170,000円的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(1700000, 2170000, 374180, 367710, 361240, 354770, 348310, 341840, 335370, 328910, 0.4084, true);

-- 插入2,170,000円 ~ 2,210,000円的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(2170000, 2210000, 571570, 565090, 558630, 552160, 545690, 539230, 532760, 526290, 0.4084, true);

-- 插入2,210,000円 ~ 2,250,000円的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(2210000, 2250000, 593340, 586870, 580410, 573930, 567470, 561010, 554540, 548070, 0.4084, true);

-- 插入2,250,000円 ~ 3,500,000円的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(2250000, 3500000, 615120, 608650, 602190, 595710, 589250, 582790, 576310, 569850, 0.4084, true);

-- 插入3,500,000円以上（无上限）的税率档位
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(3500000, NULL, 1125620, 1119150, 1112690, 1106210, 1099750, 1093290, 1086810, 1080350, 0.45945, true);

-- 添加最低收入档位（88,000円以下免税）
INSERT INTO tax_brackets (min_income, max_income, base_tax_0, base_tax_1, base_tax_2, base_tax_3, base_tax_4, base_tax_5, base_tax_6, base_tax_7, tax_rate, is_active) VALUES
(0, 88000, 0, 0, 0, 0, 0, 0, 0, 0, 0.0000, true);

-- 更新现有的0-740,000档位，确保数据正确
UPDATE tax_brackets 
SET base_tax_0 = 23520, base_tax_1 = 20130, base_tax_2 = 16740, base_tax_3 = 13350, base_tax_4 = 9960, base_tax_5 = 6570, base_tax_6 = 3180, base_tax_7 = 0
WHERE min_income = 0 AND max_income = 740000;
