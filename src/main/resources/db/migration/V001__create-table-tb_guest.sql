CREATE TABLE tb_guest
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(14)
) engine = InnoDB
  default charset = utf8;