package br.com.test.vr.web.filter;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import br.com.test.vr.data.repository.BusinessRepository;
import br.com.test.vr.model.entity.Business;
import br.com.test.vr.web.attribute.BusinessAttribute;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BusinessFilter implements WebFilter {
	
	@Autowired
	private BusinessRepository businessRepository;
	
	private PathPattern pathPattern;
	
	public BusinessFilter() {
		pathPattern = new PathPatternParser().parse("/business/sale/**");
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		
//		if(pathPattern.matches(exchange.getRequest().getPath().pathWithinApplication())) {
//			var header = exchange.getRequest().getHeaders().getFirst("X-Business");
//			if(Objects.isNull(header) || header.isBlank()) {
//				return Mono.error(new ResponseStatusException(HttpStatus.BAD_GATEWAY));
//			}
//			Optional<Business> businessOptional = businessRepository.findById(Long.parseLong(header));
//			if(businessOptional.isPresent()) {				
//				var business = businessOptional.get();
//				exchange.getAttributes().put("business", new BusinessAttribute(business.getId(), business.getName()));
//			} else {
//				return Mono.error(new ResponseStatusException(HttpStatus.BAD_GATEWAY));
//			}
//		}
//		
//		return chain.filter(exchange);
		
//		return Mono.just(exchange.getRequest().getPath().pathWithinApplication())
//		.flatMap(path-> Mono.just(pathPattern.matches(path)))
//		.map(matches-> getHeaderIfMatches(matches, exchange))
//		.flatMap(headOptional-> headOptional.map(Mono::just).orElseGet(Mono::empty))
//		.map(this::getBusinessFromHeader)
//		.flatMap(businessOptional-> businessOptional.map(Mono::just).orElseGet(Mono::empty))
//		.map(business-> fillAttributeWithBusiness(exchange, business))
//		.then(chain.filter(exchange));
		
		log.info("FILTER[BUSINESS] - filter - START");
		
		return Mono.just(exchange.getRequest().getPath().pathWithinApplication())
				.flatMap(path-> Mono.just(pathPattern.matches(path)))
				.map(matches-> getHeaderIfMatches(matches, exchange))
				.flatMap(headOptional-> headOptional.map(Mono::just).orElseGet(Mono::empty))
				.map(this::getBusinessFromHeader)
				.flatMap(businessOptional-> businessOptional.map(Mono::just).orElseGet(Mono::empty))
				.map(business-> fillAttributeWithBusiness(exchange, business))
				.then(chain.filter(exchange));
	}
	
	private BusinessAttribute fillAttributeWithBusiness(ServerWebExchange exchange, Business business) {
		BusinessAttribute businessAttribute = new BusinessAttribute(business.getId(), business.getName());
		exchange.getAttributes().put("business", businessAttribute);
		log.info("FILTER[BUSINESS] - fillAttributeWithBusines - businessAttribute[{}, {}, {}]", businessAttribute.getId(), businessAttribute.getName());
		return businessAttribute;
	}
	
	private Optional<String> getHeaderIfMatches(boolean matches, ServerWebExchange exchange) {
		log.info("FILTER[BUSINESS] - getHeaderIfMatches - matches[{}]", matches);
		if(matches) {			
			return Optional.<String>ofNullable(exchange.getRequest().getHeaders().getFirst("X-Business"));
		}
		return Optional.<String>empty();
	}
	
	private Optional<Business> getBusinessFromHeader(String headerForId) {
		log.info("FILTER[BUSINESS] - getBusinessFromHeader - headerForId[{}]", headerForId);
		return businessRepository.findById(Long.parseLong(headerForId));
	}

}
