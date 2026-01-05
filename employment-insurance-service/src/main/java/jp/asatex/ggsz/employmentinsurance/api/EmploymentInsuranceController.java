package jp.asatex.ggsz.employmentinsurance.api;

import jp.asatex.ggsz.employmentinsurance.dto.EmploymentInsuranceDto;
import jp.asatex.ggsz.employmentinsurance.service.EmploymentInsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/employment-insurance")
@RequiredArgsConstructor
public class EmploymentInsuranceController {

    private final EmploymentInsuranceService employmentInsuranceService;

    @GetMapping("/calculate")
    public Mono<ResponseEntity<EmploymentInsuranceDto>> calculateEmploymentInsurance(
            @RequestParam Integer monthlySalary,
            @RequestParam(defaultValue = "GENERAL") String employmentType) {
        
        log.info("收到雇佣保险计算请求: monthlySalary={}, employmentType={}", monthlySalary, employmentType);
        
        return employmentInsuranceService.calculateEmploymentInsurance(monthlySalary, employmentType)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("处理雇佣保险计算请求时发生错误: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .doOnSuccess(response -> log.debug("成功处理雇佣保险计算请求"));
    }
}
