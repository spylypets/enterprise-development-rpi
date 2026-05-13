package com.collections.repository;

import java.util.List;

import com.collections.model.CollectionItem;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CollectionItemRepository implements PanacheRepository<CollectionItem>{
	
	public Uni<List<CollectionItem>> findByCountry(String country) {
		return find("country", country).list();
	}
	
	public Uni<List<CollectionItem>> findByYears(int fromYear, int toYear) {
		return find("year BETWEEN :fromYear AND :toYear", Parameters.with("fromYear", fromYear).and("toYear", toYear)).list();
	}
	
	public Uni<List<CollectionItem>> findByTopic(String topic) {
		return find("array_contains(topics, :topic)", Parameters.with("topic", topic)).list();
	}

}
