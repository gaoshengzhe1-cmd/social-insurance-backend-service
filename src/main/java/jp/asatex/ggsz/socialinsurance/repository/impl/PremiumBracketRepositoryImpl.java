package jp.asatex.ggsz.socialinsurance.repository.impl;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import jp.asatex.ggsz.socialinsurance.entity.PremiumBracket;
import jp.asatex.ggsz.socialinsurance.repository.CustomPremiumBracketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;

@Repository
@RequiredArgsConstructor
public class PremiumBracketRepositoryImpl implements CustomPremiumBracketRepository {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final DatabaseClient databaseClient;

    public static final BiFunction<Row, RowMetadata, PremiumBracket> MAPPING_FUNCTION = (row, rowMetaData) -> 
        PremiumBracket.builder()
            .id(row.get("id", Long.class))
            .grade(row.get("grade", String.class))
            .stdRem(row.get("std_rem", Integer.class))
            .minAmount(row.get("min_amount", Integer.class))
            .maxAmount(row.get("max_amount", Integer.class))
            .healthNoCare(row.get("health_no_care", BigDecimal.class))
            .healthCare(row.get("health_care", BigDecimal.class))
            .pension(row.get("pension", BigDecimal.class))
            .createdAt(row.get("created_at", LocalDateTime.class))
            .updatedAt(row.get("updated_at", LocalDateTime.class))
            .build();

    @Override
    public Flux<PremiumBracket> saveAllBrackets(Iterable<PremiumBracket> premiumBrackets) {
        return Flux.fromIterable(premiumBrackets)
                .flatMap(this::saveWithTimestamp);
    }

    @Override
    public Mono<PremiumBracket> findBracketByAmount(Integer amount) {
        String sql = """
            SELECT * FROM premium_bracket 
            WHERE min_amount <= :amount 
            AND (max_amount >= :amount OR max_amount = 999999999)
            LIMIT 1
            """;
            
        return databaseClient.sql(sql)
                .bind("amount", amount)
                .map(MAPPING_FUNCTION)
                .first();
    }

    @Override
    public Mono<PremiumBracket> updatePremiumBracket(PremiumBracket premiumBracket) {
        premiumBracket.setUpdatedAt(LocalDateTime.now());
        return r2dbcEntityTemplate.update(premiumBracket);
    }

    private Mono<PremiumBracket> saveWithTimestamp(PremiumBracket premiumBracket) {
        if (premiumBracket.getId() == null) {
            premiumBracket.setCreatedAt(LocalDateTime.now());
        }
        premiumBracket.setUpdatedAt(LocalDateTime.now());
        return r2dbcEntityTemplate.insert(premiumBracket);
    }
}
