package br.com.test.vr.web.filter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import br.com.test.vr.data.repository.UserRepository;
import br.com.test.vr.model.entity.User;
import br.com.test.vr.web.attribute.UserAttribute;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserFilter implements WebFilter {
	
	@Autowired
	private UserRepository userRepository;
	
	private PathPattern pathPattern;
	
	public UserFilter() {
		pathPattern = new PathPatternParser().parse("/sale/**");
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		log.info("FILTER[USER] - filter - START");
		return Mono.just(exchange.getRequest().getPath().pathWithinApplication())
				.flatMap(path-> Mono.just(pathPattern.matches(path)))
				.map(matches-> getHeaderIfMatches(matches, exchange))
				.flatMap(headOptional-> headOptional.map(Mono::just).orElseGet(Mono::empty))
				.map(this::getUserFromHeader)
				.flatMap(businessOptional-> businessOptional.map(Mono::just).orElseGet(Mono::empty))
				.map(business-> fillAttributeWithUser(exchange, business))
				.then(chain.filter(exchange));
	}
	
	private UserAttribute fillAttributeWithUser(ServerWebExchange exchange, User user) {
		UserAttribute userAttribute = new UserAttribute(user.getId(), user.getName(), user.getEmail());
		exchange.getAttributes().put("user", userAttribute);
		log.info("FILTER[USER] - fillAttributeWithUser - userAttribute[{}, {}, {}]", userAttribute.getId(), userAttribute.getName(), userAttribute.getEmail());
		return userAttribute;
	}
	
	private Optional<String> getHeaderIfMatches(boolean matches, ServerWebExchange exchange) {
		log.info("FILTER[USER] - getHeaderIfMatches - matches[{}]", matches);
		if(matches) {		
			return Optional.<String>ofNullable(exchange.getRequest().getHeaders().getFirst("X-User"));
		}
		return Optional.<String>empty();
	}
	
	private Optional<User> getUserFromHeader(String headerForId) {
		log.info("FILTER[USER] - getUserFromHeader - headerForId[{}]", headerForId);
		return userRepository.findById(Long.parseLong(headerForId));
	}

}
