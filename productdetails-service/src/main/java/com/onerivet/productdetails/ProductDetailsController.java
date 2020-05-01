package com.onerivet.productdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductDetailsController {

	@Autowired
	private ProductDetailService productDetailService;

	@GetMapping("product-details/{id}")
	public <T> T getProductDetails(@PathVariable int id) {
		return productDetailService.callProductServiceAndGetData(id);

	}
}
