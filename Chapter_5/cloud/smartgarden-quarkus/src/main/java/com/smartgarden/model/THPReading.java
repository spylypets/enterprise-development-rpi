package com.smartgarden.model;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

@Entity
@Table(name="thp_readings", schema="weather")
public class THPReading extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "thpSequence",
            sequenceName = "thp_readings_id_seq",
            schema = "weather",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "thpSequence")
    public Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime created;

	public Float temperature;

	public Float humidity;

	public Float pressure;

	@Override
	public int hashCode() {
		return Objects.hash(created, humidity, pressure, temperature);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		THPReading other = (THPReading) obj;
		return Objects.equals(created, other.created) && Objects.equals(humidity, other.humidity)
				&& Objects.equals(pressure, other.pressure) && Objects.equals(temperature, other.temperature);
	}

	@Override
    public String toString() {
        return "THPReading{id=" + id + ", created=" + created + ", temperature=" + temperature + ", humidity=" + humidity + ", pressure=" + pressure + "}";
    }

}
