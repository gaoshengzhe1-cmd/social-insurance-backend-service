package jp.asatex.ggsz.pensioninsurance.service;

import jp.asatex.ggsz.pensioninsurance.dto.PensionInsuranceDto;
import reactor.core.publisher.Mono;

public interface PensionInsuranceService {
    Mono<PensionInsuranceDto> calculatePensionInsurance(Integer monthlySalary);
}
