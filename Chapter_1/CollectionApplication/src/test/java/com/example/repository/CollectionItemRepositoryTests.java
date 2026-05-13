package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import com.example.model.CollectionItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DataR2dbcTest
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
		Flux<CollectionItem> result = collectionItemRepository.findByYearInterval(1993, 2000);
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
		Mono<CollectionItem> result = collectionItemRepository.getById(2L);
		assertNotNull(result);
		CollectionItem testItem = result.block();
		assertEquals("Juke", testItem.name());
	    assertEquals("The Juke stature", testItem.summary());
	    assertEquals(new BigDecimal("1000000.00"), testItem.price());
	    assertEquals("arts", testItem.topics()[0]);
	    assertEquals("programming", testItem.topics()[1]);
	}
	
	@Test
	public void testAddNewItem() {
		String[] topics = {"programming"};
		CollectionItem newItem = CollectionItem.of(0, new BigDecimal("10000.00"), "The Flying Tux", "Test summary", "Test description", (short) 1991, "fi", topics);
		Mono<CollectionItem> result = collectionItemRepository.saveWithImages(newItem);
		assertNotNull(result);
		CollectionItem addedItem = result.block();
		assertEquals("The Flying Tux", addedItem.name());
	    assertEquals("Test summary", addedItem.summary());
	    assertEquals("Test description", addedItem.description());
	    assertEquals("fi", addedItem.country());
	    assertEquals(new BigDecimal("10000.00"), addedItem.price());
	    assertEquals("programming", addedItem.topics()[0]);
	}
	
	@Test
	public void testUpdateItem() {
		String[] updatedTopics = {"arts","programming","software"};
        CollectionItem testItem = collectionItemRepository.getById(2L).block();
		Mono<CollectionItem> result = collectionItemRepository.update(
			CollectionItem.of(2L, new BigDecimal("2000000.00"), testItem.image(), testItem.smallImage(), "Juke Forever!", testItem.summary(), testItem.description(), testItem.year(), testItem.country(), updatedTopics)
		).flatMap(n -> {
					return collectionItemRepository.getById(2L).flatMap(i -> {
						return Mono.just(i);
					});
		});
		assertNotNull(result);
		CollectionItem updatedItem = result.block();
		assertEquals("Juke Forever!", updatedItem.name());
	    assertEquals("The Juke stature", updatedItem.summary());
	    assertEquals(new BigDecimal("2000000.00"), updatedItem.price());
	    assertEquals("arts", updatedItem.topics()[0]);
	    assertEquals("programming", updatedItem.topics()[1]);
	    assertEquals("software", updatedItem.topics()[2]);
	}
}