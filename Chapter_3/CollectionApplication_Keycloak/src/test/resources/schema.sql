drop sequence if exists CollectionItems_seq;
create sequence CollectionItems_seq
increment by 1
start with 5;

drop sequence if exists Images_seq;
create sequence Images_seq
increment by 1
start with 10;

drop schema if exists collection cascade;
create schema collection;

create table collection.items (
        id bigint not null default nextval('CollectionItems_seq'),
        name varchar(255),
		summary varchar(1000),
		description text,
 		country varchar(2),
		creation_year smallint,
		price numeric(10,2),
		small_image bigint default 0,
		image bigint default 0,
		topics text array, 
        primary key (id)
);

create table collection.images (
        id bigint not null default nextval('Images_seq'),
		file_name varchar(255),
        primary key (id)
);