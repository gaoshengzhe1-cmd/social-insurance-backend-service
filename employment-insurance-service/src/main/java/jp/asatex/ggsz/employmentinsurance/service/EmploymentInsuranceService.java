package jp.asatex.ggsz.employmentinsurance.service;

import jp.asatex.ggsz.employmentinsurance.dto.EmploymentInsuranceDto;
import reactor.core.publisher.Mono;

public interface EmploymentInsuranceService {
    Mono<EmploymentInsuranceDto> calculateEmploymentInsurance(Integer monthlySalary, String employmentType);
}
