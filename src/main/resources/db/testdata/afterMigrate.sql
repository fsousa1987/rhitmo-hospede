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

INSERT INTO tb_guest(id, name, email, phone)
VALUES (1, 'Marina dos Santos', 'marina@gmail.com', '99 99999-9999');

INSERT INTO tb_guest(id, name, email, phone)
VALUES (2, 'Mariana dos Santos', 'mariana@hotmail.com', '99 99999-9999');

INSERT INTO tb_guest(id, name, email, phone)
VALUES (3, 'Davi dos Santos', 'davi@outlook.com', '99 99999-9999');

INSERT INTO tb_guest(id, name, email, phone)
VALUES (4, 'Gabriele dos Santos', 'gabriele@yahoo.com', '99 99999-9999');

INSERT INTO tb_reservation(code, reservation_date, checkin_date, checkout_date, total_value, room_id, room_reserved,
                           days_reserved, status, guest_id)
VALUES ('0bad13fc-e6a2-4f38-8fe7-4f6b2afa5932', '2023-09-03', '2023-09-05', '2023-09-08', 458, 1, 356, 3, 'RESERVED',
        1);

INSERT INTO tb_reservation(code, reservation_date, checkin_date, checkout_date, total_value, room_id, room_reserved,
                           days_reserved, status, guest_id)
VALUES ('0bad13fc-e6a2-4f38-8fe7-4f6b2afa5933', '2023-09-03', '2023-09-05', '2023-09-08', 458, 2, 356, 3, 'RESERVED',
        1);

INSERT INTO tb_reservation(code, reservation_date, checkin_date, checkout_date, total_value, room_id, room_reserved,
                           days_reserved, status, guest_id)
VALUES ('0bad13fc-e6a2-4f38-8fe7-4f6b2afa5934', '2023-09-03', '2023-09-05', '2023-09-08', 458, 3, 356, 3, 'RESERVED',
        2);

INSERT INTO tb_reservation(code, reservation_date, checkin_date, checkout_date, total_value, room_id, room_reserved,
                           days_reserved, status, guest_id)
VALUES ('0bad13fc-e6a2-4f38-8fe7-4f6b2afa5935', '2023-09-03', '2023-09-05', '2023-09-08', 458, 4, 356, 3, 'RESERVED',
        3);