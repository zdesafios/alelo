package br.com.test.vr.web.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateSaleRequest {
	
	@NotNull(message = "Price cannot be null")
	@Positive(message = "Price cannot be negative or zero")
	private Double price;
	
	@NotNull(message = "User id cannot be null")
	@Positive(message = "User id cannot be negative or zero")
	private Long userId;
}
