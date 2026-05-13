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

INSERT INTO collection.IMAGES(id, file_name) values(1, 'juke-small.jpg');
INSERT INTO collection.IMAGES(id, file_name) values(2, 'juke.jpg');
INSERT INTO collection.IMAGES(id, file_name) values(3, 'penny-black-small.png');
INSERT INTO collection.IMAGES(id, file_name) values(4, 'penny-black.png');
INSERT INTO collection.IMAGES(id, file_name) values(5, 'juggling-juke-small.jpg');
INSERT INTO collection.IMAGES(id, file_name) values(6, 'juggling-juke.jpg');

INSERT INTO collection.ITEMS (id, name, summary, description, creation_year, country, price, small_image, image, topics) values(1, 'The Penny Black', 'The very first stamp', 'The very first post stamp but surprisingly not the most expensive one', 1840, 'uk', 1000, 3, 4, ARRAY['history']);
INSERT INTO collection.ITEMS (id, name, summary, description, creation_year, country, price, small_image, image, topics) values(2, 'Juke', 'The Juke stature', 'Porcelain stature of Juke', 1996, 'us', 1000000, 1, 2, ARRAY['arts', 'programming']);
INSERT INTO collection.ITEMS (id, name, summary, description, creation_year, country, price, small_image, image, topics) values(3, 'Juggling Juke', 'The Juggling Juke painting', 'Post modernist oil painting of the juggling Juke', 2000, 'us', 2000000, 5, 6, ARRAY['arts', 'programming']);