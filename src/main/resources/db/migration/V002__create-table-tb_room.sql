CREATE TABLE tb_room
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    number      INT                                                                                             NOT NULL UNIQUE,
    guests      INT                                                                                             NOT NULL,
    status      ENUM ('ACTIVE_BOOKING', 'PRE_BOOKING', 'BOOKING_CANCELED', 'AVAILABLE', 'RESERVED', 'CLEANING') NOT NULL,
    description VARCHAR(255)                                                                                    NOT NULL,
    daily_value DECIMAL(10, 2)                                                                                  NOT NULL
) engine = InnoDB
  default charset = utf8;