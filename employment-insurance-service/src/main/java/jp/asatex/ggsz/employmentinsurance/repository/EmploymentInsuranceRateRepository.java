package jp.asatex.ggsz.employmentinsurance.repository;

import jp.asatex.ggsz.employmentinsurance.entity.EmploymentInsuranceRate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmploymentInsuranceRateRepository extends R2dbcRepository<EmploymentInsuranceRate, Long> {
    Mono<EmploymentInsuranceRate> findByEmploymentTypeAndActiveTrue(EmploymentInsuranceRate.EmploymentType employmentType);
}
