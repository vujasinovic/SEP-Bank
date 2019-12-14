insert into Client(id, first_name, last_name) values (1, 'John', 'Doe');
insert into Client(id, first_name, last_name) values (2, 'Anthony', 'Brambulini');
insert into Client(id, first_name, last_name) values (3, 'Felattio', 'Penana');

insert into Account(id, client, amount) values (1, 1, 140.00);
insert into Account(id, client, amount) values (2, 1, 320.91);
insert into Account(id, client, amount) values (3, 2, 321.22);
insert into Account(id, client, amount) values (4, 2, 194569.32);
insert into Account(id, client, amount) values (5, 3, 1928423479.00);
insert into Account(id, client, amount) values (6, 3, 69.91);

insert into Card(id, holder_name, pan, security_code, valid_to, account) values (1, 'John Doe',  '1234-1235-1234-1234', 666, '2021-12-12', 1);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (2, 'John Doe',  '4321-4321-4321-4321', 666, '2021-04-12', 1);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (3, 'Anthony Brambulini',  '1111-1111-1111-1111', 222, '2021-02-12', 2);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (4, 'Anthony Brambulini',  '1111-1111-1111-2222', 333, '2021-07-29', 2);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (5, 'Felattio Penana',  '3333-1111-1111-2222', 555, '2021-07-29', 3);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (6, 'Felattio Penana',  '4444-1111-1111-2222', 444, '2021-07-29', 3);


