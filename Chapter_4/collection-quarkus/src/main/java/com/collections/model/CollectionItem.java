package com.collections.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="items", schema="collection")
public class CollectionItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	BigDecimal price;
	
	@OneToOne(cascade={CascadeType.ALL}, orphanRemoval=true)
	@JoinColumn(name="small_image", unique=true)
	Image smallImage;
	
	@OneToOne(cascade={CascadeType.ALL}, orphanRemoval=true)
	@JoinColumn(name="image", unique=true)
	Image image;
	
	String name;
	
	String summary;
	
	String description;
	
	@Column(name="creation_year")
	Short year;
	
	String country;
	
	String[] topics;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(topics);
		result = prime * result + Objects.hash(country, description, id, image, name, price, smallImage, summary, year);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollectionItem other = (CollectionItem) obj;
		return Objects.equals(country, other.country) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(image, other.image) && Objects.equals(name, other.name)
				&& Objects.equals(price, other.price) && Objects.equals(smallImage, other.smallImage)
				&& Objects.equals(summary, other.summary) && Arrays.equals(topics, other.topics)
				&& Objects.equals(year, other.year);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Image getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(Image smallImage) {
		this.smallImage = smallImage;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Short getYear() {
		return year;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

}
