package com.smartgarden.service;

import java.util.Objects;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.hibernate.reactive.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@IfBuildProperty(name = "recording.service.type", stringValue = "rest")
public class RestRecordingService implements RecordingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestRecordingService.class);
	
	@RestClient
    THPRestClient thpRestClient;
	
	@ConfigProperty(name = "thp.sensor.url", defaultValue="http://localhost:8088")
	String thpSensorUrl;

	@Override
	public void storeTHPMeasurements() {
		try {
			LOGGER.info("Measure air temperature, humidity, pressure");
			thpRestClient.getMeasuredData(thpSensorUrl).subscribe().with(
			curReading -> {
				LOGGER.info("measured data: " + Objects.toString(curReading));
				Panache.withTransaction(curReading::persist).subscribe().with(persistedReading -> LOGGER.info("stored data: " + persistedReading));
			});
		}  catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
	}
}