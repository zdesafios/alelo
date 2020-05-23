package br.com.test.vr.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.test.vr.service.SaleService;
import br.com.test.vr.web.attribute.UserAttribute;
import br.com.test.vr.web.dto.SaleDto;
import br.com.test.vr.web.dto.request.AddTagRequest;
import br.com.test.vr.web.dto.response.EditedResourceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sale")
public class SaleController {
	
	@Autowired
	private SaleService saleService;
	
	@GetMapping
	public Flux<SaleDto> list(@RequestAttribute("user") UserAttribute user) {
		return saleService.listAllByUser(user);
	}
	
	@GetMapping("/{saleId}")
	public Mono<SaleDto> details(
			@RequestAttribute("user") UserAttribute user,
			@PathVariable("saleId") Long saleId) {
		return saleService.details(user, saleId);
	}
	
	@PutMapping("/{saleId}/tags")
	public Mono<EditedResourceResponse> addTag(
		@RequestAttribute("user") UserAttribute user,
		@PathVariable("saleId") Long saleId,
		@Valid @RequestBody AddTagRequest request) {
		return saleService.addTag(user, saleId, request);
	}
	
}
