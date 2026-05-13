package com.example.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="IMAGES" , schema="collection")
public record Image (
		@Id
		long id,

		@Column("file_name")
		String fileName
		)
{
	
	public static Image of(long id, String fileName) {
		return new Image(id, fileName);
	}
	
	public static Image fromRow(Map<String, Object> row, String keyColumn) {
		String prefix = keyColumn.substring(0, keyColumn.indexOf('_') + 1);
		if(row.get(keyColumn) != null) {
			return Image.of(Long.parseLong(row.get(keyColumn).toString()), (String) row.get(prefix + "file_name"));
		} else {
			return null;
		}
	}
}