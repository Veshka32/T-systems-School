insert into user values (1, "stas", "$2a$10$Yzz.g09WkIeQk1u8pdkehuO9nx1XxVxQj9Rds5j8ifTFrnNfZfGNe");
insert into user_roles values (1, "ROLE_MANAGER");
insert into numbergenerator values (1, 9990000001);

INSERT INTO test.client (id, address, birthday, email, name, passportId, surname)
VALUES (1, 'Rotterdam, Netherlands', '1930-05-11', 'coolalgs@nl.com', 'Edsger', '1111111111', 'Dijkstra');
INSERT INTO test.client (id, address, birthday, email, name, passportId, surname)
VALUES (3, 'Milwaukee, Wisconsin, U.S.', '1938-01-10', 'computer_art@us.com', 'Donald', '2222222222', 'Knuth');

INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (5, 'Add Pluto to the Solar Sustem region', 'I love Pluto', 9.00, 9.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (6, 'Provide compability with long-distance services', 'Deep space', 0.00, 0.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (8, 'Get notification about black holes approaching', 'Black Hole Alert', 15.50, 300.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (9, 'Prevent recieving calls and messages from future', 'Spolier ban', 0.00, 28.95);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (10, 'Enable communication through time', 'Tachyon protocol', 1200.00, 0.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (14, 'Enable send and receive hologram messages', 'Hologram', 3.33, 0.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost) VALUES
  (16, 'Don''t worry about time paradox issues while communicating with past and future!', 'Time paradox protection',
   42.42, 24.24);


INSERT INTO test.tariff (id, description, name, price)
VALUES (2, 'Unlimited calls and messages on Earth and to/from objects orbiting the earth', 'Mother Earth', 4.54);
INSERT INTO test.tariff (id, description, name, price)
VALUES (3, 'Unlimited calls and messages within the Solar System (Pluto excluded)', 'Solar System', 180.00);
INSERT INTO test.tariff (id, description, name, price)
VALUES (4, 'Calls and messages within the visible part of Universe', 'Star trek', 1701.00);
INSERT INTO test.tariff (id, description, name, price)
VALUES (17, 'Unlimited calls and messages within the orbit of specific planet', 'Home planet', 99.99);

INSERT INTO test.contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id, user_id)
VALUES (1, false, false, 9990000001, 1, 2, null);
INSERT INTO test.contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id, user_id)
VALUES (4, false, false, 9990000002, 3, 17, null);
