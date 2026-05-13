package com.example.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.model.CollectionItem;
import com.example.model.Image;

@Service
public class KafkaProducerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

	@Value("${kafka.producer-topic}")
    private String  topic;
    ;
    private final KafkaTemplate<String, CollectionItem> kafkaTemplate;
    private final KafkaTemplate<String, Image> kafkaImageTemplate;

    public KafkaProducerService(KafkaTemplate<String, CollectionItem> kafkaTemplate, KafkaTemplate<String, Image> kafkaImageTemplate) {
        this.kafkaTemplate = kafkaTemplate;
		this.kafkaImageTemplate = kafkaImageTemplate;
    }

    public CollectionItem sendItem(CollectionItem item) {
        kafkaTemplate.send(topic, item);
        LOGGER.info("Collection update sent: " + item);
		return item;
    }
    
    public Image sendImage(Image image) {
    	kafkaImageTemplate.send(topic, image);
        LOGGER.info("Collection update sent: " + image);
		return image;
    }
}