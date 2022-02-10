package com.webFlux.cosmos.SpringRepo.resilience;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;

@SpringBootApplication
@DependsOn("expressionResolver")
public class WebFluxCosmosSpringRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxCosmosSpringRepoApplication.class, args);
		System.setProperty("COSMOS.QUERYPLAN_CACHING_ENABLED", "true");
	}

}
