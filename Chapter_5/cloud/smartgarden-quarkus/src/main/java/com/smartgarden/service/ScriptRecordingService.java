package com.smartgarden.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartgarden.model.THPReading;

import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.hibernate.reactive.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@IfBuildProperty(name = "recording.service.type", stringValue = "system")
public class ScriptRecordingService implements RecordingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptRecordingService.class);
	
	@ConfigProperty(name = "thp.python.script", defaultValue = "thp_sensor.py")
	String thpScript;
		
	@Inject
    ScriptRunner scriptRunner;
    
    @Inject
    ObjectMapper objectMapper;
    
	@Override
	public void storeTHPMeasurements() {
		try {
			LOGGER.info("Measure air temperature, humidity, pressure");
			String reading = scriptRunner.executePythonScript(thpScript, null);
			LOGGER.info("measured data: " + reading);
			THPReading curReading = objectMapper.readValue(reading, THPReading.class);
			Panache.withTransaction(curReading::persist).subscribe().with(persistedReading -> LOGGER.info("stored data: " + persistedReading));
		} catch(Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
	}
}
