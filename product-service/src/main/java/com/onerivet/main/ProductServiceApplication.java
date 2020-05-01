package com.onerivet.main;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.*")
@EnableJpaRepositories(basePackages = "com.*")
@ComponentScan(basePackages = "com.*")
@EntityScan(basePackages = "com.onerivet.*")
@EnableSwagger2
@EnableCircuitBreaker
@EnableRetry
@EnableDiscoveryClient
public class ProductServiceApplication {

	Logger logger = LoggerFactory.getLogger(ProductServiceApplication.class);

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.onerivet")).build();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public List<RetryListener> retryListeners() {

		return Collections.singletonList(new RetryListenerSupport() {

			@Override
			public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
					Throwable throwable) {
				logger.info("Invoking Retryable method {} with {} attempt", context.getAttribute("context.name"),context.getRetryCount());
				logger.warn("Retryable method {} threw {}th exception {}", context.getAttribute("context.name"),
						context.getRetryCount(), throwable.getMessage());
			}
		});
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
