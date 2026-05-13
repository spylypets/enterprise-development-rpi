package com.smartgarden;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.resteasy.reactive.RestPath;

import com.smartgarden.model.THPReading;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@ResourceProperties(path = "/thp")
public interface THPReadingResource extends PanacheEntityResource<THPReading , Long>{
	
	@GET
	@Path("/data/{records}")
	public default  Uni<List<THPReading>>getLatestReadings(@RestPath("records") short records) {
		return THPReading.<THPReading>find("ORDER BY created DESC LIMIT :rows", Map.of("rows", records)).list()
				.onItem().transform(readings -> readings.stream().sorted((rec1,  rec2) -> rec1.created.compareTo(rec2.created)).collect(Collectors.toList()));
		
	}

}
