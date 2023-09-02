insert into tb_guest (email, name, phone) values ('test@test.com.br', 'Marcos dos Santos', '(61) 98444-4614');
insert into tb_guest (email, name, phone) values ('maria@test.com.br', 'Maria de Souza', '(61) 98444-5812');
insert into tb_guest (email, name, phone) values ('pedro@test.com.br', 'Roberto Fernandes', '(61) 98444-1331');
insert into tb_guest (email, name, phone) values ('thiago@test.com.br', 'Thiago Farias', '(61) 98444-1897');

insert into tb_room(daily_value, description, guests, number, status) values (589, 'Good room', 3, 458, 'AVAILABLE');
insert into tb_room(daily_value, description, guests, number, status) values (385, 'Nice room', 5, 323, 'RESERVED');
insert into tb_room(daily_value, description, guests, number, status) values (897, 'Bad room', 2, 899, 'RESERVED');
insert into tb_room(daily_value, description, guests, number, status) values (258, 'Nice room', 1, 587, 'BOOKING_CANCELED');

insert into tb_reservation(code, checkin_date, checkout_date, days_reserved, reservation_date, room_reserved, status, total_value, guest_id, room_id)
values (587, '2023-09-01', '2023-09-03', 3, '2023-08-16', 589, 'AVAILABLE', 4789.00, 1, 1);

insert into tb_reservation(code, checkin_date, checkout_date, days_reserved, reservation_date, room_reserved, status, total_value, guest_id, room_id)
values (587, '2023-09-01', '2023-09-03', 3, '2023-08-16', 458, 'AVAILABLE', 4789.00, 2, 2);

insert into tb_reservation(code, checkin_date, checkout_date, days_reserved, reservation_date, room_reserved, status, total_value, guest_id, room_id)
values (587, '2023-09-01', '2023-09-03', 3, '2023-08-16', 589, 'AVAILABLE', 4789.00, 3, 3);