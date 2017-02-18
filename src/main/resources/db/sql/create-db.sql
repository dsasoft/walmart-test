--DROP TABLE IF EXISTS maps CASCADE;

--DROP TABLE IF EXISTS routes CASCADE;

CREATE TABLE maps(
	id INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR(40) NOT NULL
);

CREATE TABLE routes(
	id INTEGER,
	mapId INTEGER,
	origin VARCHAR(40),
	destination VARCHAR(40),
	distance INTEGER
);

ALTER TABLE routes ADD FOREIGN KEY (mapId) REFERENCES maps(id);

--INSERT INTO maps (id, name) VALUES (1, 'My Map 01');
--INSERT INTO routes (mapId, origin, destination, distance) VALUES (1, 'A', 'B', 10);
--INSERT INTO routes (mapId, origin, destination, distance) VALUES (1, 'B', 'D', 15);
