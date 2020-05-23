package br.com.test.vr.web.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaleDto {
	private Long id;
	
	private Long userId;
	
	private Long businessId;

	private Double price;
	
	private LocalDateTime date;
	
	private List<String> tags;
	
	private String status;
	

}
