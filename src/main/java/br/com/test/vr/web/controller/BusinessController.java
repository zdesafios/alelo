package br.com.test.vr.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.test.vr.service.SaleService;
import br.com.test.vr.web.attribute.BusinessAttribute;
import br.com.test.vr.web.dto.request.CreateSaleRequest;
import br.com.test.vr.web.dto.response.CreatedResourceResponse;
import br.com.test.vr.web.dto.response.EditedResourceResponse;
import br.com.test.vr.web.dto.response.RemovedResourceResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/business/sale")
public class BusinessController {
	
	@Autowired
	private SaleService saleService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<CreatedResourceResponse> create(
		@RequestAttribute("business") BusinessAttribute business,
		@Validated @RequestBody CreateSaleRequest request) {
		
		return saleService.create(business, request);	
	}
	
	@PutMapping("/{saleId}/reverse")
	public Mono<EditedResourceResponse> reverse(
			@RequestAttribute("business") BusinessAttribute user,
			@PathVariable("saleId") Long saleId) {
		
		return saleService.reverse(saleId);
	}
	
	@DeleteMapping("/{saleId}")
	public Mono<RemovedResourceResponse> remove(
			@RequestAttribute("business") BusinessAttribute user,
			@PathVariable("saleId") Long saleId) {
		
		return saleService.remove(saleId);
	}
	
}
