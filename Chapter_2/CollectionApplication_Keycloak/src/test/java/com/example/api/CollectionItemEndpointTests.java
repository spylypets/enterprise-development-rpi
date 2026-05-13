package com.example.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.model.CollectionItem;
import com.example.security.SecurityConfig;
import com.example.service.CollectionItemService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(CollectionItemController.class)
@Import(SecurityConfig.class)
public class CollectionItemEndpointTests {

	@Autowired
	private WebTestClient webClient;
	
	@MockitoBean
	private CollectionItemService collectionItemService;
	
	@Test
	void testGetByCountry() {
		CollectionItem testItem = CollectionItem.of(0, BigDecimal.valueOf(1000), null, null, "The Penny Black", "The very first post stamp.", null, null, null, null);
		Flux<CollectionItem> testResult = Flux.fromIterable(List.of(testItem));
		given(collectionItemService.getByCountry("uk")).willReturn(testResult);
		webClient
		.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "collection")))
		.get()
		.uri("/api/collection/items/country/uk")
		.header(HttpHeaders.ACCEPT, "application/json")
		.exchange()
		.expectStatus().is2xxSuccessful()
		.expectBodyList(CollectionItem.class)
		.value(result -> {
			Assertions.assertThat(result).hasSize(1);
			Assertions.assertThat(result).element(0)
				.hasFieldOrPropertyWithValue("name", "The Penny Black")
				.hasFieldOrPropertyWithValue("price", BigDecimal.valueOf(1000))
				.hasFieldOrPropertyWithValue("summary", "The very first post stamp.");
		});
		//Try to filter by non-exisiting country 
		webClient
		.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "collection")))
		.get()
		.uri("/api/collection/items/country/xx")
		.header(HttpHeaders.ACCEPT, "application/json")
		.exchange()
		.expectStatus().is2xxSuccessful()
		.expectBodyList(CollectionItem.class)
		.value(result -> {
			Assertions.assertThat(result).hasSize(0);
		});
	}
	
	@Test
	void testGetById() {
		//Set up the test response
		CollectionItem testItem = CollectionItem.of(1, BigDecimal.valueOf(1000), null, null, "The Penny Black", "The very first post stamp.", null, null, null, null);
		Mono<CollectionItem> testResult = Mono.just(testItem);
		given(collectionItemService.getById(1L)).willReturn(testResult);
		//Call the end point
		webClient
		.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "collection")))
		.get()
		.uri("/api/collection/items/1")
		.header(HttpHeaders.ACCEPT, "application/json")
		.exchange()
		.expectStatus().is2xxSuccessful()
		.expectBody(CollectionItem.class)
		.value(result -> {
			Assertions.assertThat(result)
				.hasFieldOrPropertyWithValue("name", "The Penny Black")
				.hasFieldOrPropertyWithValue("price", BigDecimal.valueOf(1000))
				.hasFieldOrPropertyWithValue("summary", "The very first post stamp.");
		});
		//Try to find by non-exisiting ID 
		webClient
		.mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "collection")))
		.get()
		.uri("/api/collection/items/0")
		.header(HttpHeaders.ACCEPT, "application/json")
		.exchange()
		.expectStatus().is2xxSuccessful()
		.expectBody(CollectionItem.class)
		.value(result -> {
			Assertions.assertThat(result).isNull();
		});
	}
	
	@Test
	void testGetByTopic() {
		//Test implementation ...
	}
	
	@Test
	void testGetByYears() {
		//Test implementation ...
	}
 
}