package jp.asatex.ggsz.socialinsurance.api;

import jp.asatex.ggsz.socialinsurance.api.dto.SocialInsuranceDto;
import jp.asatex.ggsz.socialinsurance.application.SocialInsuranceApplicationService;
import jp.asatex.ggsz.socialinsurance.application.dto.SocialInsuranceApplicationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 社会保险API控制器
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
@RequiredArgsConstructor
public class SocialInsuranceController {

    private final SocialInsuranceApplicationService socialInsuranceApplicationService;

    /**
     * 查询社会保险金额
     *
     * @param monthlySalary 月薪
     * @param age 年龄
     * @return 包含社会保险金额的响应实体
     */
    @GetMapping("/socialInsuranceQuery")
    public Mono<ResponseEntity<SocialInsuranceDto>> socialInsuranceQuery(
            @RequestParam Integer monthlySalary,
            @RequestParam Integer age) {
        
        log.info("收到社会保险金额查询请求: monthlySalary={}, age={}", monthlySalary, age);
        
        return socialInsuranceApplicationService.socialInsuranceQuery(monthlySalary, age)
                .map(this::convertToApiDto)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("处理社会保险金额查询请求时发生错误: {}", e.getMessage(), e);
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .doOnSuccess(response -> log.debug("成功处理社会保险金额查询请求"));
    }

    /**
     * 将应用层DTO转换为API层DTO
     */
    private SocialInsuranceDto convertToApiDto(SocialInsuranceApplicationDto applicationDto) {
        return SocialInsuranceDto.builder()
                .healthCostWithNoCare(applicationDto.getHealthCostWithNoCare())
                .careCost(applicationDto.getCareCost())
                .pension(applicationDto.getPension())
                .employmentInsurance(applicationDto.getEmploymentInsurance())
                .incomeTax(applicationDto.getIncomeTax())
                .build();
    }
}
