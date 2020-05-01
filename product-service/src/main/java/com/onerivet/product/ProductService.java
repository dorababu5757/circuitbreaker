package com.onerivet.product;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.onerivet.product.exception.NotFoundException;

@Service
public class ProductService {
	Logger logger = LoggerFactory.getLogger(ProductService.class);
	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@HystrixCommand
	@Retryable(value = { SQLServerException.class }, maxAttempts = 3, backoff = @Backoff(delay = 25000))
	public List<Product> getProductList() {
		return productRepository.findAll();
	}

	@Recover
	public List<Product> getDataFallBack() {
		logger.info("Invoking FallBack method after all retry mechanism failed to recover ");
		Product product = new Product();
		product.setId(000);
		product.setPrice(0.0);
		product.setProductCode("fallback-CODE");
		product.setProductName("fallback-PRODUCT");
		product.setProductDetails("fallback-ProdcutDetails");
		List<Product> fallbackProducts = new ArrayList<>();
		fallbackProducts.add(product);
		return fallbackProducts;

	}

	public Product getProduct(int id) throws NotFoundException {
		return productRepository.findById(id).orElseThrow(NotFoundException::new);
	}
}
