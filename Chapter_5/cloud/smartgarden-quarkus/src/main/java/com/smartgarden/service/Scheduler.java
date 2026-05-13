package com.smartgarden.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@IfBuildProperty(name = "scheduler.enabled", stringValue = "true")
public class Scheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
	
    @Inject
    RecordingService recordingService;

    @Scheduled(every = "{scheduler.measurement-interval}", delayed="1s")
    Uni<Void> storeMeasurement() {
        try {
        	recordingService.storeTHPMeasurements();		}
        catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		return Uni.createFrom().voidItem();
    }
}