package com.example.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.CollectionItem;
import com.example.service.CollectionItemService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/collection/items")
public class CollectionItemController {
	
	private final CollectionItemService service;
	
	public CollectionItemController(CollectionItemService service) {
		this.service = service;
	}
	
	@GetMapping("/")
	public Flux<CollectionItem> getRows(String sortBy) {
		return service.getSorted(sortBy);
	}
	
	@GetMapping("/{id}")
	public Mono<CollectionItem> getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@GetMapping("/country/{country}")
	public Flux<CollectionItem> getByCountry(@PathVariable String country) {
		return service.getByCountry(country);
	}
	
	@GetMapping("/topic/{topic}")
	public Flux<CollectionItem> getByTopic(@PathVariable String topic) {
		return service.getByTopic(topic);
	}
	
	@GetMapping("/fromyear/{fromyear}/toyear/{toyear}")
	public Flux<CollectionItem> getByYears(@PathVariable("fromyear") short fromYear, @PathVariable("toyear") short toYear) {
		return service.getByYears(fromYear, toYear);
	}

}