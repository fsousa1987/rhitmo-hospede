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
) engine = InnoDB
  default charset = utf8;