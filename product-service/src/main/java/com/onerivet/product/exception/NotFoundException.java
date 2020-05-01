package com.onerivet.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "NotFound")
public class NotFoundException extends Exception {


	private static final long serialVersionUID = 3079917778299259360L;
	
	public NotFoundException() {
		super();
	}
	

}
