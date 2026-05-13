package com.example.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;

import com.example.ApplicationContextUtils;
import com.example.model.CollectionItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollectionItemRepository extends R2dbcRepository<CollectionItem, Long> {
	
	static final DatabaseClient dbClient = ApplicationContextUtils.getApplicationContext().getBean(DatabaseClient.class);
	
	static final String ITEM_SELECT_QUERY = """
			SELECT i.id i_id,i.price i_price,i.name i_name,i.summary i_summary,i.description i_description,i.creation_year i_creation_year,i.country i_country, i.topics i_topics,
				   im.id im_id,im.file_name im_file_name,
				   sim.id sim_id,sim.file_name sim_file_name
				FROM collection.ITEMS i
				JOIN collection.IMAGES im ON im.id=i.image
				JOIN collection.IMAGES sim ON sim.id=i.small_image
				""";
	
	default Flux<CollectionItem> findByCountry(String country) {
		return dbClient.sql(String.format("%s WHERE i.country = :cntry", ITEM_SELECT_QUERY))
				.bind("cntry", country)
				.fetch().all()
				.map(CollectionItem::fromRow);
	}
	
	default Flux<CollectionItem> findByYearInterval(int startYear, int endYear) {
		return dbClient.sql(String.format("%s WHERE i.creation_year BETWEEN :startYear AND :endYear", ITEM_SELECT_QUERY))
				.bind("startYear", startYear)
				.bind("endYear", endYear)
				.fetch().all()
				.map(CollectionItem::fromRow);
	}

	default Flux<CollectionItem> findByTopic(String topic) {
		return dbClient.sql(String.format("%s WHERE lower(:topic) = ANY (i.topics)", ITEM_SELECT_QUERY))
				.bind("topic", topic)
				.fetch().all()
				.map(CollectionItem::fromRow);
	}
	
	default	Mono<CollectionItem> findById(Long id) {
		return dbClient.sql(String.format("%s WHERE i.id = :id", ITEM_SELECT_QUERY))
				.bind("id", id)
				.fetch().one()
				.map(CollectionItem::fromRow);
	}
	
}