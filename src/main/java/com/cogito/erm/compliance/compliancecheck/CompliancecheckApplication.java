package com.cogito.erm.compliance.compliancecheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CompliancecheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompliancecheckApplication.class, args);
	}
}
