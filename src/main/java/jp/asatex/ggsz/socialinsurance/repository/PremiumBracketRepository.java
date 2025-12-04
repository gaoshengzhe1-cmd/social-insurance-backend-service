package jp.asatex.ggsz.socialinsurance.repository;

import jp.asatex.ggsz.socialinsurance.entity.PremiumBracket;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * 社会保险费等级数据访问接口
 */
@Repository
public interface PremiumBracketRepository extends R2dbcRepository<PremiumBracket, Long>, CustomPremiumBracketRepository {
    
    /**
     * 根据等级查询保险费信息
     * @param grade 等级
     * @return 包含查询结果的Mono对象
     */
    Mono<PremiumBracket> findByGrade(String grade);
    
    /**
     * 根据标准报酬范围查询保险费信息
     * @param amount 标准报酬金额
     * @return 包含查询结果的Flux对象
     */
    Flux<PremiumBracket> findByMinAmountLessThanEqualAndMaxAmountGreaterThanEqual(Integer amount, Integer amount2);
    
    /**
     * 根据标准报酬查询对应的保险费等级
     * @param stdRem 标准报酬
     * @return 包含查询结果的Mono对象
     */
    Mono<PremiumBracket> findByStdRem(Integer stdRem);
}
