package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.CollectionItem;
import com.example.repository.CollectionItemRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollectionItemService {
	
	private final CollectionItemRepository repository;
	
	public CollectionItemService(CollectionItemRepository repository) {
		this.repository = repository;
	}
	
	public Flux<CollectionItem> getSorted(String country) {
		return repository.findAll();
	}
	
	public Flux<CollectionItem> getByCountry(String country) {
		return repository.findByCountry(country);
	}
	
	public Flux<CollectionItem> getByTopic(String topic) {
		return repository.findByTopic(topic);
	}
	
	public Flux<CollectionItem> getByYears(short fromYear, short toYear) {
		return repository.findByYearInterval(fromYear, toYear);
	}

	public Mono<CollectionItem> getById(Long id) {
		return repository.findById(id);
	}

}