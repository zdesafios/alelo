package br.com.test.vr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.test.vr.data.repository.SaleRepository;
import br.com.test.vr.mapper.SaleMapper;
import br.com.test.vr.model.SaleStatus;
import br.com.test.vr.web.attribute.BusinessAttribute;
import br.com.test.vr.web.attribute.UserAttribute;
import br.com.test.vr.web.dto.SaleDto;
import br.com.test.vr.web.dto.request.AddTagRequest;
import br.com.test.vr.web.dto.request.CreateSaleRequest;
import br.com.test.vr.web.dto.response.CreatedResourceResponse;
import br.com.test.vr.web.dto.response.EditedResourceResponse;
import br.com.test.vr.web.dto.response.RemovedResourceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SaleService {
	
	@Autowired
	private SaleRepository repository;
	
	@Autowired
	private SaleMapper saleMapper;

	public Flux<SaleDto> listAllByUser(UserAttribute user) {
		return Mono.fromSupplier(()-> repository.findByUserId(user.getId()))
		.flatMapMany(Flux::fromIterable)
		.map(saleMapper::entityToDto);
	}
	
	public Mono<SaleDto> details(UserAttribute user, Long saleId) {
		return Mono.fromSupplier(()-> repository.findById(saleId))
		.flatMap(saleOptional-> saleOptional.map(Mono::just).orElseGet(Mono::empty))
		.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
		.map(saleMapper::entityToDto);
	}
	
	public Mono<CreatedResourceResponse> create(BusinessAttribute user, CreateSaleRequest request) {
		return Mono.fromSupplier(()-> saleMapper.requestToEntity(user, request))
		.doOnSuccess(repository::save)
		.flatMap(sale-> Mono.just(new CreatedResourceResponse(sale.getId())));
	}
	
	public Mono<EditedResourceResponse> addTag(UserAttribute user, Long saleId, AddTagRequest request) {
		return Mono.fromSupplier(()-> repository.findById(saleId))
		.flatMap(saleOptional-> saleOptional.map(Mono::just).orElseGet(Mono::empty))
			.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
		.doOnSuccess(sale-> {
			sale.addTag(request.getTag());
			repository.save(sale);
		}).flatMap(editedSale-> Mono.just(new EditedResourceResponse(editedSale.getId())));
	}
	
	public Mono<EditedResourceResponse> reverse(Long saleId) {
		return Mono.fromSupplier(()-> repository.findById(saleId))
		.flatMap(saleFromDatabaseAsOptional-> saleFromDatabaseAsOptional.map(Mono::just).orElseGet(Mono::empty))
			.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
		.doOnSuccess(sale-> {
			sale.setStatus(SaleStatus.REVERSED);
			repository.save(sale);
		}).flatMap(editedSale-> Mono.just(new EditedResourceResponse(editedSale.getId())));
	}
	
	public Mono<RemovedResourceResponse> remove(Long saleId) {
		return Mono.fromSupplier(()-> repository.findById(saleId))
		.flatMap(saleOptional-> saleOptional.map(Mono::just).orElseGet(Mono::empty))
			.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
		.doOnSuccess(repository::delete)
		.flatMap(deletedSale-> Mono.just(new RemovedResourceResponse()));
	}
	
}
