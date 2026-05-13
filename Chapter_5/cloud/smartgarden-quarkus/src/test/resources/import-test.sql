CREATE SCHEMA IF NOT EXISTS weather;

CREATE SEQUENCE IF NOT EXISTS weather.thp_readings_id_seq INCREMENT BY 1 START WITH 1;
CREATE TABLE IF NOT EXISTS weather.thp_readings (
    id bigint PRIMARY KEY,
    created timestamp without time zone,
    temperature numeric(3,1),
    humidity numeric(4,1),
    pressure numeric(8,2)
);

INSERT INTO weather.thp_readings (id, created, temperature, humidity, pressure) VALUES (89161, '2024-04-08 09:00:58', 23.4, 62.0, 964.45);