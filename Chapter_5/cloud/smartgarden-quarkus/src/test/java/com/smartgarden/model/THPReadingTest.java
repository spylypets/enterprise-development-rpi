package com.smartgarden.model;

import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@QuarkusTest
public class THPReadingTest {

	@Test
	@RunOnVertxContext
	public void testCreateReading(TransactionalUniAsserter asserter) {
		THPReading reading = new THPReading();
		LocalDateTime now = LocalDateTime.now();
		reading.created = now;
		reading.temperature = 20.2f;
		reading.humidity = 65.00f;
		reading.pressure = 970.00f;

		asserter.<THPReading>assertThat(() -> reading.persist(),
				r -> {
					Assertions.assertNotNull(r.id);
					asserter.putData("reading.id", r.id);
				});
				asserter.assertThat(() -> THPReading.<THPReading>findById(
				asserter.getData("reading.id")), persistedReading -> {
					Assertions.assertNotNull(persistedReading);
					Assertions.assertEquals(reading.temperature, persistedReading.temperature);
					Assertions.assertEquals(reading.humidity, persistedReading.humidity);
					Assertions.assertEquals(reading.pressure, persistedReading.pressure);
				});
	}
}