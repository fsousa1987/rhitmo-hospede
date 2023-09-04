CREATE TABLE tb_reservation
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    code             VARCHAR(255)                                                                                    NOT NULL,
    reservation_date DATE                                                                                            NOT NULL,
    checkin_date     DATE                                                                                            NOT NULL,
    checkout_date    DATE                                                                                            NOT NULL,
    total_value      DECIMAL(10, 2),
    room_id          BIGINT,
    room_reserved    INT,
    days_reserved    INT,
    status           ENUM ('ACTIVE_BOOKING', 'PRE_BOOKING', 'BOOKING_CANCELED', 'AVAILABLE', 'RESERVED', 'CLEANING') NOT NULL,
    guest_id         BIGINT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE tb_reservation
    ADD CONSTRAINT fk_room_id FOREIGN KEY (room_id) REFERENCES tb_room (id);
ALTER TABLE tb_reservation
    ADD CONSTRAINT fk_guest_id FOREIGN KEY (guest_id) REFERENCES tb_guest (id);