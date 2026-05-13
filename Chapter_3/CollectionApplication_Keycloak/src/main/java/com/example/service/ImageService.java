package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Image;
import com.example.repository.ImageRepository;
import com.example.service.kafka.KafkaProducerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ImageService {
	
	private final ImageRepository repository;
	private final KafkaProducerService producerService;
	
	public ImageService(ImageRepository repository, KafkaProducerService producerService) {
		this.repository = repository;
		this.producerService = producerService;
	}
	
	public Flux<Image> getSorted(String sortBy) {
		return repository.findAll();
	}
	
	@Transactional
	public Mono<Image> add(Image image) {
		return repository.save(image)
				.flatMap(i -> {
						producerService.sendImage(i);
						return Mono.just(i);
				});
	}

}
