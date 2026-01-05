package jp.asatex.ggsz.pensioninsurance.repository.impl;

import jp.asatex.ggsz.pensioninsurance.entity.PensionBracket;
import jp.asatex.ggsz.pensioninsurance.repository.PensionBracketRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PensionBracketRepositoryImpl implements PensionBracketRepository {

    private final DatabaseClient databaseClient;

    public PensionBracketRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<PensionBracket> findBracketByAmount(Integer amount) {
        String sql = "SELECT * FROM pension_bracket WHERE min_amount <= :amount AND max_amount > :amount LIMIT 1";
        
        return databaseClient.sql(sql)
                .bind("amount", amount)
                .map(row -> {
                    PensionBracket bracket = new PensionBracket();
                    bracket.setId(row.get("id", Long.class));
                    bracket.setGrade(row.get("grade", String.class));
                    bracket.setStdRem(row.get("std_rem", Integer.class));
                    bracket.setMinAmount(row.get("min_amount", Integer.class));
                    bracket.setMaxAmount(row.get("max_amount", Integer.class));
                    bracket.setPension(row.get("pension", java.math.BigDecimal.class));
                    bracket.setCreatedAt(row.get("created_at", java.time.LocalDateTime.class));
                    bracket.setUpdatedAt(row.get("updated_at", java.time.LocalDateTime.class));
                    return bracket;
                })
                .one();
    }
}
