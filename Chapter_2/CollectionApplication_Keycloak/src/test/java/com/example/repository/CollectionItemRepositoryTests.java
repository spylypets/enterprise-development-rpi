package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.model.CollectionItem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
public class CollectionItemRepositoryTests {

  @Autowired
  CollectionItemRepository collectionItemRepository;


    @Test
	public void testFindByCountry() {
		Flux<CollectionItem> result = collectionItemRepository.findByCountry("uk");
		assertNotNull(result);
		assertEquals(1, result.count().block());
		CollectionItem first = result.blockFirst();
		assertEquals("The Penny Black", first.name());
		assertEquals("history", first.topics()[0]);
	}
	
	@Test
	public void testFindByYearInterval() {
		Flux<CollectionItem> result = collectionItemRepository.findByYearInterval(1990, 2000);
		assertNotNull(result);
		assertEquals(2, result.count().block());
		CollectionItem first = result.blockFirst();
		assertEquals("Juke", first.name());
	    assertEquals("The Juke stature", first.summary());
	    assertEquals(new BigDecimal("1000000.00"), first.price());
	    assertEquals("arts", first.topics()[0]);
	    //Check the item images
	    assertEquals("juke.jpg", first.image().fileName());
	    assertEquals("juke-small.jpg", first.smallImage().fileName());
	}
	
	@Test
	public void testFindByTopic() {
		Flux<CollectionItem> result = collectionItemRepository.findByTopic("Arts");
		assertNotNull(result);
		assertEquals(2, result.count().block());
		CollectionItem first = result.blockFirst();
		assertEquals("Juke", first.name());
	    assertEquals("The Juke stature", first.summary());
	    assertEquals(new BigDecimal("1000000.00"), first.price());
	    assertEquals("arts", first.topics()[0]);
	}
	
	@Test
	public void testFindById() {
		Mono<CollectionItem> result = collectionItemRepository.findById(2L);
		assertNotNull(result);
		CollectionItem testItem = result.block();
		assertEquals("Juke", testItem.name());
	    assertEquals("The Juke stature", testItem.summary());
	    assertEquals(new BigDecimal("1000000.00"), testItem.price());
	    assertEquals("arts", testItem.topics()[0]);
	    assertEquals("programming", testItem.topics()[1]);
	}
}