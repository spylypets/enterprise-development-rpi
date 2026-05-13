package com.collections.service;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.collections.model.CollectionItem;
import com.collections.repository.CollectionItemRepository;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class CollectionItemService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionItemService.class);
	
	@Inject
	CollectionItemRepository repository;
	
	@Inject
    @Channel("collection-output")
    Emitter<CollectionItem> messageEmitter;

	@WithSession
	public Uni<List<CollectionItem>> findByCountry(String country) {
		return repository.findByCountry(country);
	}

	@WithSession
	public Uni<List<CollectionItem>> findByYears(int fromYear, int toYear) {
		return repository.findByYears(fromYear, toYear);
	}

	@WithSession
	public Uni<List<CollectionItem>> findByTopic(String topic) {
		return repository.findByTopic(topic);
	}
	
	@WithSession
	public Uni<CollectionItem> findById(Long id) {
		return repository.findById(id);
	}

	@WithTransaction
	public Uni<CollectionItem> add(CollectionItem item) {
			return repository.persist(item)
					.onFailure()
							.invoke(e -> { LOGGER.error("Error while persisting: " + item.getName(), e); })
					.onFailure()
							.transform(e -> { throw new WebApplicationException("Persistence error!", 409); })
					.onItem().call(persistedItem -> {
						messageEmitter.send(persistedItem);
						return Uni.createFrom().item(persistedItem);
					});
	}
	
	@WithTransaction
	public Uni<CollectionItem> putUpdate(CollectionItem updatedItem, Long id) {
		return repository.findById(id).onItem().ifNull().failWith(new WebApplicationException("Item not found!", 404)) 
				.invoke(item -> {
					item.setName(updatedItem.getName());
					item.setSummary(updatedItem.getSummary());
					item.setDescription(updatedItem.getDescription());
					item.setPrice(updatedItem.getPrice());
					item.setYear(updatedItem.getYear());
					item.setCountry(updatedItem.getCountry());
					if (updatedItem.getImage() != null) {
						if(item.getImage() != null) {
							item.getImage().setFileName(updatedItem.getImage().getFileName());
						} else {
							item.setImage(updatedItem.getImage());
						}
					}
					if (updatedItem.getSmallImage() != null) {
						
						if(item.getSmallImage() != null) {
							item.getSmallImage().setFileName(updatedItem.getSmallImage().getFileName());
						} else {
							item.setSmallImage(updatedItem.getSmallImage());
						}
					}
					item.setTopics(updatedItem.getTopics());
					}).flatMap(
						item -> repository.persistAndFlush(item)
							.onFailure()
							.invoke(e -> { LOGGER.error("Error while updating: " + updatedItem.getName(), e); })
							.onFailure()
							.transform(e -> { throw new WebApplicationException("Persistence error!", 409); })
								.onItem().call(i -> {
								    messageEmitter.send(i);
								    return Uni.createFrom().item(i);
								})
						);
	}
	
	@WithTransaction
	public Uni<CollectionItem> patchUpdate(CollectionItem updatedItem, Long id) {
		return repository.findById(id).onItem().ifNull().failWith(new WebApplicationException("Item not found!", 404)) 
				.invoke(item -> {
					if(updatedItem.getName() != null) item.setName(updatedItem.getName());
					if(updatedItem.getSummary() != null) item.setSummary(updatedItem.getSummary());
					if(updatedItem.getDescription() != null) item.setDescription(updatedItem.getDescription());
					if(updatedItem.getPrice() != null) item.setPrice(updatedItem.getPrice());
					if(updatedItem.getYear() != null) item.setYear(updatedItem.getYear());
					if(updatedItem.getCountry() != null) item.setCountry(updatedItem.getCountry());
					if(updatedItem.getTopics() != null) item.setTopics(updatedItem.getTopics());
					if (updatedItem.getImage() != null) {
						if(item.getImage() != null) {
							item.getImage().setFileName(updatedItem.getImage().getFileName());
						} else {
							item.setImage(updatedItem.getImage());
						}
					}
					if (updatedItem.getSmallImage() != null) {
						
						if(item.getSmallImage() != null) {
							item.getSmallImage().setFileName(updatedItem.getSmallImage().getFileName());
						} else {
							item.setSmallImage(updatedItem.getSmallImage());
						}
					}
					}).flatMap(
						item -> repository.persistAndFlush(item)
							.onFailure()
							.invoke(e -> { LOGGER.error("Error while PATCH updating: " + updatedItem.getName(), e); })
							.onFailure()
							.transform(e -> { throw new WebApplicationException("Persistence error!", 409); })
								.onItem().call(i -> {
								    messageEmitter.send(i);
								    return Uni.createFrom().item(i);
								})
						);
	}

	@WithTransaction
	public Uni<Integer> updateProperties(Map<String, Object> paramMap) {
		if (!paramMap.containsKey("id")) {
			LOGGER.warn("No ID provided.");
			return Uni.createFrom().item(0);
		}
		StringBuilder query = new StringBuilder();
		String whereClause = " WHERE id=:id";
		paramMap.forEach((k,v) -> {
			if (!k.equals("id")) {
				query.append(k).append("=:").append(k).append(",");
			}
		});
		String queryStr = query.deleteCharAt(query.length() - 1).append(whereClause).toString();
		LOGGER.debug(queryStr);
		return repository.update(queryStr, paramMap);
	}

	@WithTransaction
	public Uni<Boolean> delete(Long id) {
		return repository.deleteById(id);
	}

}
