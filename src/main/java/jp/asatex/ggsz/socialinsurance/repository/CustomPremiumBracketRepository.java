package jp.asatex.ggsz.socialinsurance.repository;

import jp.asatex.ggsz.socialinsurance.entity.PremiumBracket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 自定义保险费等级数据访问接口
 */
public interface CustomPremiumBracketRepository {
    
    /**
     * 批量保存保险费等级信息
     * @param premiumBrackets 保险费等级列表
     * @return 保存后的保险费等级Flux
     */
    Flux<PremiumBracket> saveAllBrackets(Iterable<PremiumBracket> premiumBrackets);
    
    /**
     * 根据标准报酬范围查询对应的保险费等级
     * @param amount 标准报酬金额
     * @return 包含查询结果的Mono对象
     */
    Mono<PremiumBracket> findBracketByAmount(Integer amount);
    
    /**
     * 更新保险费等级信息
     * @param premiumBracket 保险费等级实体
     * @return 更新后的Mono对象
     */
    Mono<PremiumBracket> updatePremiumBracket(PremiumBracket premiumBracket);
}
