package jp.asatex.ggsz.socialinsurance.repository;

import jp.asatex.ggsz.socialinsurance.entity.EmploymentInsuranceRate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface EmploymentInsuranceRateRepository extends R2dbcRepository<EmploymentInsuranceRate, Long> {
    
    /**
     * Find the active employment insurance rate by employment type.
     * @param employmentType The type of employment
     * @return A Mono containing the matching rate if found
     */
    Mono<EmploymentInsuranceRate> findByEmploymentTypeAndActiveTrue(EmploymentInsuranceRate.EmploymentType employmentType);
    
    /**
     * Check if an active rate exists for the given employment type.
     * @param employmentType The type of employment
     * @param excludeId ID to exclude (for updates)
     * @return A Mono emitting true if an active rate exists for the employment type
     */
    Mono<Boolean> existsByEmploymentTypeAndActiveTrueAndIdNot(
        EmploymentInsuranceRate.EmploymentType employmentType, 
        Long excludeId
    );
}
