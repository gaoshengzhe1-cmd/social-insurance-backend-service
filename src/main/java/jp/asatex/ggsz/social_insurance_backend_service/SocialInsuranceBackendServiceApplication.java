package jp.asatex.ggsz.social_insurance_backend_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = "jp.asatex.ggsz")
@EnableR2dbcRepositories(basePackages = "jp.asatex.ggsz.socialinsurance.repository")
public class SocialInsuranceBackendServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialInsuranceBackendServiceApplication.class, args);
	}

}
