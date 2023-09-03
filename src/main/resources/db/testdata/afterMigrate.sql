# noinspection SqlWithoutWhereForFile

SET foreign_key_checks = 0;

DELETE
FROM tb_guest;

DELETE
FROM tb_room;

DELETE
FROM tb_reservation;

SET foreign_key_checks = 1;

ALTER TABLE tb_guest
    AUTO_INCREMENT = 1;

ALTER TABLE tb_reservation
    AUTO_INCREMENT = 1;

ALTER TABLE tb_room
    AUTO_INCREMENT = 1;

INSERT INTO tb_room(id, number, guests, status, description, daily_value)
VALUES (1, 356, 2, 'AVAILABLE', 'Good room', 450);

INSERT INTO tb_room(id, number, guests, status, description, daily_value)
VALUES (2, 448, 1, 'ACTIVE_BOOKING', 'Nice room', 589);

INSERT INTO tb_room(id, number, guests, status, description, daily_value)
VALUES (3, 377, 5, 'RESERVED', 'Cool room', 355);

INSERT INTO tb_room(id, number, guests, status, description, daily_value)
VALUES (4, 698, 1, 'PRE_BOOKING', 'Nice room', 1587);

INSERT INTO tb_room(id, number, guests, status, description, daily_value)
VALUES (5, 222, 7, 'AVAILABLE', 'Cool room', 700);