package com.example.repository;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;

import com.example.model.CollectionItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomizedCollectionItemRepositoryImpl implements CustomizedCollectionItemRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedCollectionItemRepositoryImpl.class);
	
	static final String ITEM_SELECT_QUERY = """
			SELECT i.id i_id,i.price i_price,i.name i_name,i.summary i_summary,i.description i_description,i.creation_year i_creation_year,i.country i_country, i.topics i_topics,
				   im.id im_id,im.file_name im_file_name,
				   sim.id sim_id,sim.file_name sim_file_name
				FROM collection.ITEMS i
				LEFT JOIN collection.IMAGES im ON im.id=i.image
				LEFT JOIN collection.IMAGES sim ON sim.id=i.small_image
				""";
	static final String ITEM_INSERT_QUERY = """
			INSERT INTO collection.ITEMS (price, name, summary, description, creation_year, country, topics, image, small_image)
			 VALUES (:price, :name, :summary, :description, :creation_year, :country, :topics, :image, :small_image)
			""";
	
	static final String ITEM_UPDATE_QUERY = """
			UPDATE collection.ITEMS SET price = :price, name = :name, summary = :summary, description = :description, creation_year = :creation_year, 
				country = :country, topics = :topics, image = :image, small_image = :small_image
				WHERE id = :id
			""";
	
	static final Map<String,String> columnMap = Map.of("price","price", "name","name", "summary","summary", "description","description",
														"year","creation_year", "country","country", "topics","topics");
	
	final DatabaseClient dbClient; 
	
	public CustomizedCollectionItemRepositoryImpl(DatabaseClient dbClient) {
		this.dbClient = dbClient;
	}

	@Override
	public Flux<CollectionItem> findAll(String sortBy) {
		if (! columnMap.containsKey(sortBy)) {
			LOGGER.warn(String.format("Unknown property to sort by: %s", sortBy));
			return Flux.empty();
		}
		return dbClient.sql(String.format("%s ORDER BY i.%s", ITEM_SELECT_QUERY, columnMap.get(sortBy)))
				.fetch().all()
				.map(CollectionItem::fromRow);
	}

	@Override
	public Flux<CollectionItem> findByCountry(String country) {
		return dbClient.sql(String.format("%s WHERE i.country = :cntry", ITEM_SELECT_QUERY))
				.bind("cntry", country)
				.fetch().all()
				.map(CollectionItem::fromRow);
	}

	@Override
	public Flux<CollectionItem> findByYearInterval(int startYear, int endYear) {
		return dbClient.sql(String.format("%s WHERE i.creation_year BETWEEN :startYear AND :endYear", ITEM_SELECT_QUERY))
				.bind("startYear", startYear)
				.bind("endYear", endYear)
				.fetch().all()
				.map(CollectionItem::fromRow);
	}

	@Override
	public Flux<CollectionItem> findByTopic(String topic) {
		return dbClient.sql(String.format("%s WHERE lower(:topic) = ANY (i.topics)", ITEM_SELECT_QUERY))
				.bind("topic", topic)
				.fetch().all()
				.map(CollectionItem::fromRow);
	}

	@Override
	public Mono<CollectionItem> getById(Long id) {
		return dbClient.sql(String.format("%s WHERE i.id = :id", ITEM_SELECT_QUERY))
				.bind("id", id)
				.fetch().one()
				.map(CollectionItem::fromRow);
	}

	@Override
	public Mono<CollectionItem> saveWithImages(CollectionItem item) {
		return dbClient.sql(ITEM_INSERT_QUERY)
				.filter(s -> s.returnGeneratedValues("id", "price", "name", "summary", "description", "creation_year",
											"country", "topics", "image", "small_image"))
						.bind("price", item.price())
						.bind("name", item.name())
						.bind("summary", item.summary())
						.bind("description", item.description())
						.bind("creation_year", item.year())
						.bind("country", item.country())
						.bind("topics", item.topics())
						.bind("image", item.image() != null && item.image().id() != null ? item.image().id() : 0)
						.bind("small_image", item.smallImage() != null && item.smallImage().id() != null ? item.smallImage().id() : 0)
						.fetch()
		                .first()
		                .map(CollectionItem::fromNewRow);
	}

	@Override
	public Mono<Long> update(CollectionItem item) {
		return dbClient.sql(ITEM_UPDATE_QUERY)
				.bind("id", item.id())
				.bind("price", item.price())
				.bind("name", item.name())
				.bind("summary", item.summary())
				.bind("description", item.description())
				.bind("creation_year", item.year())
				.bind("country", item.country())
				.bind("topics", item.topics())
				.bind("image", item.image() != null && item.image().id() != null ? item.image().id() : 0)
				.bind("small_image", item.smallImage() != null && item.smallImage().id() != null ? item.smallImage().id() : 0)
				.fetch()
				.rowsUpdated();
	}

}