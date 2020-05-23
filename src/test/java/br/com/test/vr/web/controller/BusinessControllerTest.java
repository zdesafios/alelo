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
import br.com.test.vr.model.SaleStatus;
import br.com.test.vr.model.entity.Business;
import br.com.test.vr.model.entity.User;
import br.com.test.vr.service.SaleService;
import br.com.test.vr.web.controller.BusinessController;
import br.com.test.vr.web.controller.SaleController;
import br.com.test.vr.web.core.TestLogger;
import br.com.test.vr.web.dto.SaleDto;
import br.com.test.vr.web.dto.response.CreatedResourceResponse;
import br.com.test.vr.web.error.ValidationErrorResponse;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest({BusinessController.class, SaleController.class})
@Import({ SaleService.class, SaleMapper.class, GlobalError.class })
@DirtiesContext(classMode =  DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureDataJpa
public class BusinessControllerTest {

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
	public void createSaleWithoutPriceInRequest() {

		String jsonWithoutPrice = "{\"userId\":\"123\"}";

		webClient.mutate().filter(TestLogger.logRequest()).build()
			.post()
				.uri("/business/sale")
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Business", "123")
				.body(Mono.just(jsonWithoutPrice), String.class)
			.exchange()
				.expectStatus().isBadRequest()
				.expectBody(ValidationErrorResponse.class)
				.value(error-> error.getErros().size(), equalTo(1));
	}

	@Test
	public void createSaleWithSuccess() {

		String json = "{\"price\": \"28.8\", \"userId\":\"123\"}";

		webClient.mutate().filter(TestLogger.logRequest()).build()
			.post()
				.uri("/business/sale")
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Business", "123")
				.body(Mono.just(json), String.class)
			.exchange()
				.expectStatus().isCreated()
				.expectBody(CreatedResourceResponse.class)
					.value(created-> created.getId(), equalTo(2L)); // a quantidade de sale é 2, pq já foi criado 1 antes do teste

	}
	
	@Test
	public void reverseSaleWithSucess() {

		webClient.mutate().filter(TestLogger.logRequest()).build()
			.put()
				.uri("/business/sale/1/reverse")
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Business", "123")
			.exchange()
				.expectStatus().isOk();
		
		
		webClient.mutate().filter(TestLogger.logRequest()).build()
		.get()
			.uri("/sale/1")
			.header("Content-Type", "application/json")
			.header("X-User", "123")
		.exchange()
			.expectStatus().isOk()
			.expectBody(SaleDto.class)
				.value(sale-> sale.getStatus(), equalTo(SaleStatus.REVERSED.toString()));

	}
	
	@Test
	public void deleteSaleWithSuccess() {

		webClient.mutate().filter(TestLogger.logRequest()).build()
			.delete()
				.uri("/business/sale/1")
				.header("Content-Type", "application/json")
				.header("X-Business", "123")
			.exchange()
				.expectStatus().isOk();
		
		webClient.mutate().filter(TestLogger.logRequest()).build()
		.get()
			.uri("/sale")
			.header("X-User", "123")
			.header("Content-Type", "application/json")
		.exchange()
			.expectStatus().isOk()
			.expectBodyList(SaleDto.class)
				.value(list->list.size(), equalTo(0));

	}

}
