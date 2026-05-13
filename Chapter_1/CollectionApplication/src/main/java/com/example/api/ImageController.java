package com.example.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Image;
import com.example.service.ImageService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/collection/images")
public class ImageController {
	
private final ImageService service;
	
	public ImageController(ImageService service) {
		this.service = service;
	}
	
	@GetMapping("/")
	public Flux<Image> getRows(String sortBy) {
		return service.getSorted(sortBy);
	}

	@PostMapping("/add")
    public Mono<Image> add(@RequestBody Image image) {
        return service.add(image);
    }
}
