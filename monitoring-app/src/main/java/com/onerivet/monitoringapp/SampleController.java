package com.onerivet.monitoringapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RequestMapping("/property")
@RestController
public class SampleController {
	
	@HystrixCommand
	@GetMapping
	public String testHystrix() {
		return "data";
	}

}
