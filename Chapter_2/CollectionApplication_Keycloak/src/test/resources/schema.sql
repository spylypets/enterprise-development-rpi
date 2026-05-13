CREATE SCHEMA  collection;

CREATE TABLE collection.ITEMS (
        id bigint not null,
        name varchar(255),
		summary varchar(1000),
		description text,
 		country varchar(2),
		creation_year smallint,
		price numeric(10,2),
		small_image bigint,
		image bigint,
		topics text array,
        PRIMARY KEY (id)
);

CREATE TABLE collection.IMAGES (
        id bigint not null,
		file_name varchar(255),
        PRIMARY KEY (id)
);