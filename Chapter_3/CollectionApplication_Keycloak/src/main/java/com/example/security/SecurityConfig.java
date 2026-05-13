package com.example.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    @Value("${spring.security.oauth2.resourceserver.jwt.jwkSetUri}")
    private String jwkEndpoint;
    
    @Bean 
    SecurityWebFilterChain springSecurity(ServerHttpSecurity http) {
    	http
    		.authorizeExchange((exchange) -> exchange
    			.pathMatchers("/api/**").hasAuthority("SCOPE_collection")
    			.anyExchange().authenticated())
    			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    	return http.build();
    }
    
    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkEndpoint).build();
    }
        
}