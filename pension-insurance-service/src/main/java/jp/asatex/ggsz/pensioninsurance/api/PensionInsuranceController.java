package jp.asatex.ggsz.pensioninsurance.api;

import jp.asatex.ggsz.pensioninsurance.dto.PensionInsuranceDto;
import jp.asatex.ggsz.pensioninsurance.service.PensionInsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pension-insurance")
@RequiredArgsConstructor
public class PensionInsuranceController {

    private final PensionInsuranceService pensionInsuranceService;

    @GetMapping("/calculate")
    public Mono<ResponseEntity<PensionInsuranceDto>> calculatePensionInsurance(
            @RequestParam Integer monthlySalary) {
        
        log.info("收到厚生年金计算请求: monthlySalary={}", monthlySalary);
        
        return pensionInsuranceService.calculatePensionInsurance(monthlySalary)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("处理厚生年金计算请求时发生错误: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .doOnSuccess(response -> log.debug("成功处理厚生年金计算请求"));
    }
}
