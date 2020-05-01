package com.onerivet.product;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onerivet.product.exception.NotFoundException;

@RestController
@RequestMapping("/api")
public class ProductController {

	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products")
	public List<Product> getProductList() {
		return productService.getProductList();
	}

	@GetMapping("/product/{id}")
	public Product getProduct(@PathVariable @Valid int id) throws NotFoundException {
		return productService.getProduct(id);
	}
}
