insert into Account(id, amount) values (1, 140.00);
insert into Account(id, amount) values (2, 320.91);
insert into Account(id, amount) values (3, 321.22);


insert into Client(id, first_name, last_name, merchant_id, merchant_password, account_id) values (1, 'John', 'Doe', 'merchant1', 'merchant1_password', 1);
insert into Client(id, first_name, last_name, merchant_id, merchant_password, account_id) values (2, 'Anthony', 'Brambulini', 'merchant2', 'merchant2_password', 2);
insert into Client(id, first_name, last_name, merchant_id, merchant_password, account_id) values (3, 'Felattio', 'Penana', 'merchant3', 'merchant3_password', 3);

insert into Card(id, holder_name, pan, security_code, valid_to, account) values (1, 'John Doe',  '1234-1235-1234-1234', 666, '2021-12-12', 1);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (2, 'John Doe',  '4321-4321-4321-4321', 666, '2021-04-12', 1);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (3, 'Anthony Brambulini',  '1111-1111-1111-1111', 222, '2021-02-12', 2);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (4, 'Anthony Brambulini',  '1111-1111-1111-2222', 333, '2021-07-29', 2);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (5, 'Felattio Penana',  '3333-1111-1111-2222', 555, '2021-07-29', 3);
insert into Card(id, holder_name, pan, security_code, valid_to, account) values (6, 'Felattio Penana',  '4444-1111-1111-2222', 444, '2021-07-29', 3);

-- insert into Payment(id,s amount, url, account_id) values (120.0, '1234567890', 1);


