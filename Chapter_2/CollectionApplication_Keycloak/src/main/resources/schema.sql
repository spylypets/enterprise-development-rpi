drop table if exists collection.ITEMS;
create table collection.ITEMS (
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
        primary key (id)
);

drop table if exists collection.IMAGES;
create table collection.IMAGES (
        id bigint not null,
		file_name varchar(255),
        primary key (id)
);