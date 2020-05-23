package br.com.test.vr.web.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.test.vr.data.repository.BusinessRepository;
import br.com.test.vr.data.repository.UserRepository;
import br.com.test.vr.error.GlobalError;
import br.com.test.vr.mapper.SaleMapper;
import br.com.test.vr.model.entity.Business;
import br.com.test.vr.model.entity.User;
import br.com.test.vr.service.SaleService;
import br.com.test.vr.web.controller.BusinessController;
import br.com.test.vr.web.controller.SaleController;
import br.com.test.vr.web.core.TestLogger;
import br.com.test.vr.web.dto.SaleDto;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest({SaleController.class, BusinessController.class})
@Import({ SaleService.class, SaleMapper.class, GlobalError.class })
@DirtiesContext(classMode =  DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureDataJpa
public class SaleControllerTest {
	
	@Autowired
	private WebTestClient webClient;
	
	@Autowired
	private BusinessRepository businessRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	public void before() {
		Business business = new Business();
		business.setName("business 1");
		businessRepository.save(business);
		
		User user = new User();
		user.setEmail("email@email.com");
		user.setName("user 1");
		
		userRepository.save(user);
		
		
		String json = "{\"price\": \"28.8\", \"userId\":\"123\"}";

		webClient.mutate().filter(TestLogger.logRequest()).build()
			.post()
				.uri("/business/sale")
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Business", "123")
				.body(Mono.just(json), String.class)
			.exchange()
				.expectStatus().isCreated();
		
	}

	@Test
	public void listSalesTest() {
		
		webClient.mutate().filter(TestLogger.logRequest()).build()
		.get()
			.uri("/sale")
			.header("X-User", "123")
			.header("Content-Type", "application/json")
		.exchange()
			.expectStatus().isOk()
			.expectBodyList(SaleDto.class)
				.value(list->list.size(), equalTo(1));
	}
	
	@Test
	public void salesDetailsTest() {
		webClient.mutate().filter(TestLogger.logRequest()).build()
		.get()
			.uri("/sale/1")
			.header("X-User", "123")
			.header("Content-Type", "application/json")
		.exchange()
			.expectStatus().isOk()
			.expectBody(SaleDto.class)
				.value(sale-> sale, notNullValue());
	}
	
	@Test
	public void salesDetailsWithInvalidSaleIdTest() {
		webClient.mutate().filter(TestLogger.logRequest()).build()
		.get()
			.uri("/sale/10")
			.header("X-User", "123")
			.header("Content-Type", "application/json")
		.exchange()
			.expectStatus().isNotFound();
	}
	
	@Test
	public void addTagTest() {
		String tag = "{\"tag\":\"Tag 1\"}";
		
		webClient.mutate().filter(TestLogger.logRequest()).build()
		.put()
			.uri("/sale/1/tags")
			.header("X-User", "123")
			.header("Content-Type", "application/json")
			.body(Mono.just(tag), String.class)
		.exchange()
			.expectStatus().isOk();
		
		
		webClient.mutate().filter(TestLogger.logRequest()).build()
		.get()
			.uri("/sale/1")
			.header("X-User", "123")
			.header("Content-Type", "application/json")
		.exchange()
			.expectStatus().isOk()
			.expectBody(SaleDto.class)
				.value(sale-> sale, notNullValue())
				.value(sale->sale.getTags().size(), equalTo(1));
	}
	
}
