package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.CollectionItem;
import com.example.repository.CollectionItemRepository;
import com.example.repository.CustomizedCollectionItemRepository;
import com.example.service.kafka.KafkaProducerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollectionItemService {
	
	private final CollectionItemRepository repository;
	private final KafkaProducerService producerService;
	
	public CollectionItemService(CollectionItemRepository repository, KafkaProducerService producerService) {
		this.repository = repository;
		this.producerService = producerService;
	}
	
	public Flux<CollectionItem> getSorted(String sortBy) {
		return repository.findAll(sortBy);
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
		return repository.getById(id);
	}

	@Transactional
	public Mono<CollectionItem> add(CollectionItem item) {
		return ((CustomizedCollectionItemRepository)repository).saveWithImages(item)
				.flatMap(inserted -> {
					return repository.getById(inserted.id()).flatMap(i -> {
						producerService.sendItem(i);
						return Mono.just(i);
					});
				});
	}
	
	@Transactional
	public Mono<CollectionItem> update(CollectionItem item) {
		return ((CustomizedCollectionItemRepository)repository).update(item)
				.flatMap(n -> {
						return repository.getById(item.id()).flatMap(i -> {
								producerService.sendItem(i);
								return Mono.just(i);
						});
				});
	}

	public Mono<Void> delete(Long id) {
		return repository.deleteById(id);
	}
}