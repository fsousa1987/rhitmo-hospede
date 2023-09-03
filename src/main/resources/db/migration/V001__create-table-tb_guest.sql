create table tb_guest
(
    id    bigint       not null auto_increment,
    nome  varchar(60)  not null,
    email varchar(120) not null,
    phone varchar(10)  not null,

    primary key (id)
) engine = InnoDB
  default charset = utf8;