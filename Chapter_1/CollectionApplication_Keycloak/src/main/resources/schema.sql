drop sequence if exists CollectionItems_seq;
create sequence CollectionItems_seq
increment by 1
start with 1;

drop sequence if exists Images_seq;
create sequence Images_seq
increment by 1
start with 1;

drop schema if exists collection cascade;
create schema collection;

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

create table collection.IMAGES (
        id bigint not null,
		file_name varchar(255),
        primary key (id)
);