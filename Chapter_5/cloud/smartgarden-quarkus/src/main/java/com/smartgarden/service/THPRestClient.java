package com.smartgarden.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.smartgarden.model.THPReading;

import io.quarkus.rest.client.reactive.Url;
import io.smallrye.mutiny.Uni;
	
@RegisterRestClient(baseUri = "http://localhost:8088")
@Path("/bme280")
public interface THPRestClient {
		
	@GET
	@Path("/thp")
	@Produces(MediaType.APPLICATION_JSON)
	Uni<THPReading> getMeasuredData(@Url String thpSensorUrl);
}
