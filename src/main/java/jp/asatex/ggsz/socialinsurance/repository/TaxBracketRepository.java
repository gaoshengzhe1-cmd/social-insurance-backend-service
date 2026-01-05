package jp.asatex.ggsz.socialinsurance.repository;

import jp.asatex.ggsz.socialinsurance.entity.TaxBracket;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TaxBracketRepository extends R2dbcRepository<TaxBracket, Long> {
    
    /**
     * Find the applicable tax bracket for a given income.
     * @param income The income amount to find the tax bracket for
     * @return A Mono containing the matching tax bracket if found
     */
    @Query("SELECT * FROM tax_brackets WHERE " +
           "min_income <= :income AND (max_income IS NULL OR :income < max_income) " +
           "AND is_active = true ORDER BY min_income DESC LIMIT 1")
    Mono<TaxBracket> findBracketByIncome(@Param("income") Integer income);
    
    /**
     * Check if a tax bracket exists for the given income range.
     * @param minIncome Minimum income (inclusive)
     * @param maxIncome Maximum income (exclusive)
     * @param excludeId ID to exclude (for updates)
     * @return A Mono emitting true if a conflicting tax bracket exists
     */
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tax_brackets t WHERE " +
           "t.min_income = :minIncome AND t.max_income = :maxIncome AND t.id <> :excludeId")
    Mono<Boolean> existsByMinIncomeAndMaxIncomeAndIdNot(
        @Param("minIncome") Integer minIncome,
        @Param("maxIncome") Integer maxIncome,
        @Param("excludeId") Long excludeId
    );
}
