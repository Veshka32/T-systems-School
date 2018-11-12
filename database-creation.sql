create table client
(
  id         int auto_increment
    primary key,
  address    varchar(255) null,
  birthday   date         null,
  email      varchar(255) null,
  name       varchar(255) null,
  passportId varchar(255) null,
  surname    varchar(255) null,
  constraint UK_r7jtxva660fvy31fl5x7n64sw
  unique (passportId)
)
  engine = InnoDB;

create table numbergenerator
(
  id         int auto_increment
    primary key,
  nextNumber bigint null
)
  engine = InnoDB;

create table option_
(
  id            int auto_increment
    primary key,
  description   varchar(255)   null,
  name          varchar(255)   null,
  price         decimal(19, 2) null,
  subscribeCost decimal(19, 2) null,
  constraint UK_6eagrhb6we1b859rxvlrd5ouu
  unique (name)
)
  engine = InnoDB;

create table optionrelation
(
  id         int auto_increment
    primary key,
  relation   varchar(255) null,
  another_id int          null,
  one_id     int          null,
  constraint FKnq14p7rbo1xdb9h6fkrrpc6po
  foreign key (another_id) references option_ (id),
  constraint FK2n15vo4t3qultowqrd9m0ypuo
  foreign key (one_id) references option_ (id)
)
  engine = InnoDB;

create index FK2n15vo4t3qultowqrd9m0ypuo
  on optionrelation (one_id);

create index FKnq14p7rbo1xdb9h6fkrrpc6po
  on optionrelation (another_id);

create table tariff
(
  id          int auto_increment
    primary key,
  description varchar(255)   null,
  name        varchar(255)   not null,
  price       decimal(19, 2) null,
  constraint UK_f35dwuejd10c6p0tlms3f5omq
  unique (name)
)
  engine = InnoDB;

create table contract
(
  id               int auto_increment
    primary key,
  isBlocked        bit    not null,
  isBlockedByAdmin bit    not null,
  number           bigint null,
  owner_id         int    null,
  tariff_id        int    null,
  constraint FKklqnhq4b1r3addu51h8rw1v75
  foreign key (owner_id) references client (id),
  constraint FKt1xg1b6lmjo8d0pkbg6j8rpak
  foreign key (tariff_id) references tariff (id)
)
  engine = InnoDB;

create index FKklqnhq4b1r3addu51h8rw1v75
  on contract (owner_id);

create index FKt1xg1b6lmjo8d0pkbg6j8rpak
  on contract (tariff_id);

create table contract_option
(
  contract_id int not null,
  option_id   int not null,
  primary key (contract_id, option_id),
  constraint FK2ec1vjstgerrj20h3odajs945
  foreign key (contract_id) references contract (id),
  constraint FKq4ocxp9rtibdqynvj7w46ppa6
  foreign key (option_id) references option_ (id)
)
  engine = InnoDB;

create index FKq4ocxp9rtibdqynvj7w46ppa6
  on contract_option (option_id);

create table tariff_option
(
  tariff_id int not null,
  option_id int not null,
  primary key (tariff_id, option_id),
  constraint FKbbkxqijd4oodhbctgdt3hjx7x
  foreign key (tariff_id) references tariff (id),
  constraint FKjvk2ruhhkffufdlkh7jtq847v
  foreign key (option_id) references option_ (id)
)
  engine = InnoDB;

create index FKjvk2ruhhkffufdlkh7jtq847v
  on tariff_option (option_id);

create table user
(
  id          int auto_increment
    primary key,
  login       varchar(255) not null,
  password    varchar(255) not null,
  contract_id int          null,
  constraint FK69biufib2occwfmrkjm1hxpbe
  foreign key (contract_id) references contract (id)
)
  engine = InnoDB;

create index FK69biufib2occwfmrkjm1hxpbe
  on user (contract_id);

create table user_roles
(
  user_id int          not null,
  roles   varchar(255) null,
  constraint FK55itppkw3i07do3h7qoclqd4k
  foreign key (user_id) references user (id)
)
  engine = InnoDB;

create index FK55itppkw3i07do3h7qoclqd4k
  on user_roles (user_id);


insert into user values (default, "stas", "$2a$10$Yzz.g09WkIeQk1u8pdkehuO9nx1XxVxQj9Rds5j8ifTFrnNfZfGNe", null);
insert into user_roles values (1, "ROLE_MANAGER");
#check!
insert into numbergenerator values (default, 9990000004);

INSERT INTO test.client (id, address, birthday, email, name, passportId, surname)
VALUES (default, 'Rotterdam, Netherlands', '1930-05-11', 'coolalgs@nl.com', 'Edsger', '1111111111', 'Dijkstra');
INSERT INTO test.client (id, address, birthday, email, name, passportId, surname)
VALUES (default, 'Milwaukee, Wisconsin, U.S.', '1938-01-10', 'computer_art@us.com', 'Donald', '2222222222', 'Knuth');

INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Add Pluto to the Solar System region', 'I love Pluto', 9.00, 9.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Provide compatibility with long-distance services', 'Deep space', 0.00, 0.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Get notification about black holes approaching', 'Black Hole Alert', 15.50, 300.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Prevent receiving calls and messages from future', 'Spoiler ban', 0.00, 28.95);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Enable communication through time', 'Tachyon protocol', 1200.00, 0.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost)
VALUES (default, 'Enable send and receive hologram messages', 'Hologram', 3.33, 0.00);
INSERT INTO test.option_ (id, description, name, price, subscribeCost) VALUES
  (default, 'Don''t worry about time paradox issues while communicating with past and future!',
   'Time paradox protection',
   42.42, 24.24);
INSERT INTO test.option_ (id, description, name, price, subscribeCost) values
  (default, 'Enable communication within the planet only","Local network', 10.00, 0.00);

INSERT INTO test.tariff (id, description, name, price)
VALUES (default, 'Unlimited calls and messages on Earth and to/from objects orbiting the earth', 'Mother Earth', 4.54);
INSERT INTO test.tariff (id, description, name, price)
VALUES (default, 'Unlimited calls and messages within the Solar System (Pluto excluded)', 'Solar System', 180.00);
INSERT INTO test.tariff (id, description, name, price)
VALUES (default, 'Calls and messages within the visible part of Universe', 'Star trek', 1701.00);
INSERT INTO test.tariff (id, description, name, price)
VALUES (default, 'Unlimited calls and messages within the orbit of specific planet', 'Home planet', 99.99);

INSERT INTO test.contract (id, isBlocked, isBlockedByAdmin, number, owner_id, tariff_id)
VALUES (1, false, false, 9990000003, 1, 4);

INSERT INTO test.contract_option (contract_id, option_id) VALUES (1, 3);
INSERT INTO test.contract_option (contract_id, option_id) VALUES (1, 6);

INSERT INTO test.tariff_option (tariff_id, option_id) VALUES (2, 2);
INSERT INTO test.tariff_option (tariff_id, option_id) VALUES (3, 2);
INSERT INTO test.tariff_option (tariff_id, option_id) VALUES (1, 8);
INSERT INTO test.tariff_option (tariff_id, option_id) VALUES (4, 8);

INSERT INTO test.optionrelation (id, relation, another_id, one_id) VALUES (1, 'INCOMPATIBLE', 8, 1);
INSERT INTO test.optionrelation (id, relation, another_id, one_id) VALUES (2, 'MANDATORY', 2, 1);
INSERT INTO test.optionrelation (id, relation, another_id, one_id) VALUES (3, 'INCOMPATIBLE', 8, 2);
INSERT INTO test.optionrelation (id, relation, another_id, one_id) VALUES (4, 'MANDATORY', 5, 4);
INSERT INTO test.optionrelation (id, relation, another_id, one_id) VALUES (5, 'MANDATORY', 5, 7);

#
# truncate user_roles;
# truncate contract_option;
# truncate tariff_option;
# truncate optionrelation;
# truncate numbergenerator;
# truncate user;

