#UPDATE mysql.user set user = 'stas' where user = 'root';
#FLUSH privileges;

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
  foreign key (contract_id) references contract (id),
  constraint UK_login
  unique (login)
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

