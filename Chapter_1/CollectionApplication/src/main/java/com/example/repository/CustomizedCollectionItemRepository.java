package com.example.repository;

import com.example.model.CollectionItem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomizedCollectionItemRepository {
	
	Flux<CollectionItem> findAll(String sortBy);
		
	Flux<CollectionItem> findByCountry(String country);
	
	Flux<CollectionItem> findByYearInterval(int startYear, int endYear);
	
	Flux<CollectionItem> findByTopic(String topic);
		
	Mono<CollectionItem> getById(Long id);
	
	Mono<CollectionItem> saveWithImages(CollectionItem item);
	
	Mono<Long> update(CollectionItem item);
}
