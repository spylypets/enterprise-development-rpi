package com.collections;

import com.collections.service.CollectionItemService;

import java.util.List;
import java.util.Map;

import org.jboss.resteasy.reactive.RestPath;

import com.collections.model.CollectionItem;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/collection/items")
public class CollectionItemResource {
	
	@Inject
	CollectionItemService service;
	
	@GET
	@Path("/country/{country}")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<List<CollectionItem>> getByCountry(@RestPath("country") String country) {
		return service.findByCountry(country);
	}
	
	@GET
	@Path("/fromyear/{fromyear}/toyear/{toyear}")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<List<CollectionItem>> getByYears(@RestPath("fromyear") int fromYear, @RestPath("toyear") int toYear) {
		return service.findByYears(fromYear, toYear);
	}
	
	@GET
	@Path("/topic/{topic}")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<List<CollectionItem>> getByTopic(@RestPath("topic") String topic) {
		return service.findByTopic(topic);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<CollectionItem> getById(@RestPath("id") Long id) {
		return service.findById(id);
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
    public Uni<CollectionItem> add(CollectionItem item) {
        return service.add(item);
    }
	
	@PUT
	@Path("/update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
    public Uni<CollectionItem> putUpdate(CollectionItem item, @RestPath("id") Long id) {
        return service.putUpdate(item, id);
    }
	
	@PATCH
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
    public Uni<Integer> update(Map<String, Object> paramMap) {
        return service.updateProperties(paramMap);
    }
	
	@PATCH
	@Path("/update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
    public Uni<CollectionItem> patchUpdate(CollectionItem item, @RestPath("id") Long id) {
        return service.patchUpdate(item, id);
    }
	
	@DELETE
	@Path("/delete/{id}")
    public Uni<Boolean> delete(@RestPath("id") Long id) {
        return service.delete(id);
    }

}
