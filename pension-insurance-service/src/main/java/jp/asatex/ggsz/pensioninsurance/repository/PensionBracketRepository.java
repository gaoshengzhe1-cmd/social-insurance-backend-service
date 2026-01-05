package jp.asatex.ggsz.pensioninsurance.repository;

import jp.asatex.ggsz.pensioninsurance.entity.PensionBracket;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PensionBracketRepository extends R2dbcRepository<PensionBracket, Long> {
    Mono<PensionBracket> findBracketByAmount(Integer amount);
}
