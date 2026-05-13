package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	public Flux<CollectionItem> getRows(@RequestParam("sortby") String sortBy) {
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
	
	@PostMapping("/add")
    public Mono<CollectionItem> add(@RequestBody CollectionItem item) {
        return service.add(item);
    }
	
	@PutMapping("/update/{id}")
    public Mono<CollectionItem> update(@RequestBody CollectionItem item, @PathVariable("id") Long id) {
        return service.update(item);
    }
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }

}