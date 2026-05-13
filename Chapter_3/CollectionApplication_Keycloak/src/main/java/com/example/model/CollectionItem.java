package com.example.model;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="items", schema="collection")
public record CollectionItem (
	
	@Id
	Long id,

	BigDecimal price,
	
	Image smallImage,
	
	Image image,
	
	String name,
	
	String summary,
	
	String description,
	
	@Column(value="creation_year")
	Short year,
	
	String country,
	
	Object[] topics
) {
	public static CollectionItem of(long id, BigDecimal price, Image smallImage, Image image, String name, String summary, String description, Short year, String country, Object[] topics)  {
		return new CollectionItem(id, price, smallImage, image, name, summary, description, year, country, topics);
	}
	
	public static CollectionItem of(long id, BigDecimal price, String name, String summary, String description, Short year, String country, Object[] topics)  {
		return new CollectionItem(id, price, null, null, name, summary, description, year, country, topics);
	}
	
	public static CollectionItem fromRow(Map<String, Object> row) {
		return of(
				Long.parseLong(row.get("i_id").toString()),
				(BigDecimal) row.get("i_price"),
				Image.fromRow(row, "sim_id"),
				Image.fromRow(row, "im_id"),
				(String) row.get("i_name"),
				(String) row.get("i_summary"),
				(String) row.get("i_description"),
				(Short) row.get("i_creation_year"),
				(String) row.get("i_country"),
				(Object[]) row.get("i_topics")
			);
	}
	
	public static CollectionItem fromNewRow(Map<String, Object> row) {
		return of(
				Long.parseLong(row.get("id").toString()),
				(BigDecimal) row.get("price"),
				(String) row.get("name"),
				(String) row.get("summary"),
				(String) row.get("description"),
				(Short) row.get("creation_year"),
				(String) row.get("country"),
				(Object[]) row.get("topics")
			);
	}
}