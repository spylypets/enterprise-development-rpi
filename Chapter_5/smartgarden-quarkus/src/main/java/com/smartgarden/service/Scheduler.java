package com.smartgarden.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgarden.model.THPReading;

@ApplicationScoped
@IfBuildProperty(name = "scheduler.enabled", stringValue = "true")
public class Scheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
	
	@ConfigProperty(name = "thp.python.script", defaultValue = "thp_sensor.py")
	String thpScript;

    @Inject
    ScriptRunner scriptRunner;

    @Inject
    ObjectMapper objectMapper;

    @Scheduled(every = "{scheduler.measurement-interval}", delayed="1s")
    Uni<Void> storeMeasurement() {
        try {

			String reading = scriptRunner.executePythonScript(thpScript, null);
			LOGGER.info("measured air temperature, humidity, pressure: " + reading);
			THPReading curReading = objectMapper.readValue(reading, THPReading.class);
			Panache.withTransaction(curReading::persist).subscribe().with(persistedReading -> LOGGER.info("stored data: " + persistedReading));
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return Uni.createFrom().voidItem();
    }
}