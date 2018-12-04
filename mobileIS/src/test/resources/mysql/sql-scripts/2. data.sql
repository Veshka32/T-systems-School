INSERT INTO numbergenerator (id, nextNumber) VALUES (default, 9990000007);

INSERT INTO option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Provide compatibility with long-distance services ', 'Deep space', 0.00, 0.00);
INSERT INTO option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Get notification about black holes approaching', 'Black Hole Alert', 15.50, 300.00);
INSERT INTO option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Prevent receiving calls and messages from future', 'Spolier ban', 0.00, 28.95);
INSERT INTO option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Enable communication through time', 'Tachyon protocol', 1200.00, 0.00);
INSERT INTO option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Enable send and receive hologram messages', 'Hologram', 3.33, 0.00);
INSERT INTO option_ (id, description, name, price, subscribeCost) VALUES
  (default, 'Don''t worry about time paradox issues while communicating with past and future!',
   'Time paradox protection', 42.42, 24.24);
INSERT INTO option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Enable communication within the planet only', 'Local network', 10.00, 0.00);
INSERT INTO option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Add Pluto to the Solar System region', 'I love Pluto', 9.00, 9.00);

INSERT INTO optionrelation (id, relation, another_id, one_id) VALUES (default, 'MANDATORY', 4, 6);
INSERT INTO optionrelation (id, relation, another_id, one_id) VALUES (default, 'MANDATORY', 4, 3);
INSERT INTO optionrelation (id, relation, another_id, one_id) VALUES (default, 'MANDATORY', 1, 8);
INSERT INTO optionrelation (id, relation, another_id, one_id) VALUES (default, 'INCOMPATIBLE', 1, 7);

INSERT INTO tariff (id, description, name, price)
VALUES (default, 'Unlimited calls and messages on Earth and to/from objects orbiting the earth', 'Mother Earth', 4.54);
INSERT INTO tariff (id, description, name, price)
VALUES (default, 'Unlimited calls and messages within the Solar System (Pluto excluded)', 'Solar System', 180.00);
INSERT INTO tariff (id, description, name, price)
VALUES (default, 'Calls and messages within the visible part of Universe', 'Star trek', 1701.00);
INSERT INTO tariff (id, description, name, price)
VALUES (default, 'Unlimited calls and messages within the orbit of specific planet', 'Home planet', 99.99);

INSERT INTO tariff_option (tariff_id, option_id) VALUES (1, 7);
INSERT INTO tariff_option (tariff_id, option_id) VALUES (2, 1);
INSERT INTO tariff_option (tariff_id, option_id) VALUES (3, 1);
INSERT INTO tariff_option (tariff_id, option_id) VALUES (4, 7);

INSERT INTO client (id, address, birthday, email, name, passportId, surname)
VALUES (default, 'Rotterdam, Netherlands', '1930-05-11', 'coolalgs@nl.com', 'Edsger', '1111111111', 'Dijkstra');
INSERT INTO client (id, address, birthday, email, name, passportId, surname)
VALUES (default, 'Milwaukee, Wisconsin, U.S.', '1938-01-10', 'computer_art@us.com', 'Donald', '2222222222', 'Knuth');
INSERT INTO client (id, address, birthday, email, name, passportId, surname) VALUES
  (default, 'Princeton, New Jersey, U.S.', '1946-12-20', 'princeton_rs@us.com', 'Robert', '3333333333', 'Sedgewick');
INSERT INTO client (id, address, birthday, email, name, passportId, surname)
VALUES (default, 'England, London', '1912-06-23', 'enigma@com.com', 'Alan', '4444444444', 'Turing');

INSERT INTO contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id)
VALUES (default, false, false, 9990000001, 1, 1);
INSERT INTO contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id)
VALUES (default, true, false, 9990000002, 1, 2);
INSERT INTO contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id)
VALUES (default, false, true, 9990000003, 2, 3);
INSERT INTO contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id)
VALUES (default, false, false, 9990000004, 3, 4);
INSERT INTO contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id)
VALUES (default, false, false, 9990000005, 3, 2);
INSERT INTO contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id)
VALUES (default, false, false, 9990000006, 3, 1);

INSERT INTO contract_option (contract_id, option_id) VALUES (1, 2);
INSERT INTO contract_option (contract_id, option_id) VALUES (2, 8);
INSERT INTO contract_option (contract_id, option_id) VALUES (3, 3);
INSERT INTO contract_option (contract_id, option_id) VALUES (3, 4);
INSERT INTO contract_option (contract_id, option_id) VALUES (3, 6);

INSERT INTO user (id, login, password, contract_id)
VALUES (default, 'stas', '$2a$10$Yzz.g09WkIeQk1u8pdkehuO9nx1XxVxQj9Rds5j8ifTFrnNfZfGNe', null);
INSERT INTO user (id, login, password, contract_id)
VALUES (default, '9990000001', '$2a$10$Yzz.g09WkIeQk1u8pdkehuO9nx1XxVxQj9Rds5j8ifTFrnNfZfGNe', 1);
INSERT INTO user (id, login, password, contract_id)
VALUES (default, '9990000004', '$2a$10$Yzz.g09WkIeQk1u8pdkehuO9nx1XxVxQj9Rds5j8ifTFrnNfZfGNe', 4);

INSERT INTO user_roles (user_id, roles) VALUES (1, 'ROLE_MANAGER');
INSERT INTO user_roles (user_id, roles) VALUES (2, 'ROLE_CLIENT');
INSERT INTO user_roles (user_id, roles) VALUES (3, 'ROLE_CLIENT');



