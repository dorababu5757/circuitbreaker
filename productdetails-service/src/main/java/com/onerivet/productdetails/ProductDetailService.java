package com.onerivet.productdetails;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ProductDetailService {
	Logger logger = LoggerFactory.getLogger(ProductDetailService.class);

	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@HystrixCommand(fallbackMethod = "callProductServiceAndGetDataFallBack")
	public <T> T callProductServiceAndGetData(int productId) {

		logger.info("Getting Product details for {}", productId);

		ProductDetails product = restTemplate.exchange("http://product-service/api/product/{id}", HttpMethod.GET, null,
				ProductDetails.class, productId).getBody();

		logger.info("Response Received as {} - {}", product.getProductDetails(), new Date());

		return (T) product;
	}

	@SuppressWarnings("unchecked")
	public <T> T callProductServiceAndGetDataFallBack(int productId) {
		ProductDetails product = new ProductDetails();
		product.setId(0);
		product.setProductDetails("Fall Back Details" + productId);
		if (printHystrixCommandMetrics()) {
			return (T) ("CIRCUIT BREAKER ENABLED!!! No Response From Product Service at this moment. "
					+ " Service will be back shortly - " + new Date());
		}
		return (T) product;
	}

	public boolean printHystrixCommandMetrics() {
		boolean isCircuitOpen = false;
		for (HystrixCommandMetrics metrics : HystrixCommandMetrics.getInstances()) {
			isCircuitOpen = HystrixCircuitBreaker.Factory.getInstance(metrics.getCommandKey()).isOpen();

			logger.info("group:{}, commandKey:{}, CircuitOpen:{}, Mean:{}, 95%:{}, 99%:{}, 99.5%:{}, {}",
					metrics.getCommandGroup().name(), metrics.getCommandKey().name(), isCircuitOpen,
					metrics.getExecutionTimeMean(), metrics.getExecutionTimePercentile(95.0),
					metrics.getExecutionTimePercentile(99.5), metrics.getExecutionTimePercentile(99.5),
					metrics.getHealthCounts());

		}
		return isCircuitOpen;
	}
}
