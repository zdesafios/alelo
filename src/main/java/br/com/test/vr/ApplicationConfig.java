package br.com.test.vr;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableCaching
@EnableWebFlux
public class ApplicationConfig {

}
