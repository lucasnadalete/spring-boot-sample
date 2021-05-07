CREATE TABLE IF NOT EXISTS fatec_user
(
    id   serial8       NOT NULL,
    name varchar(255) NOT NULL,
    age  integer      NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO fatec_user(name, age)
values ('Douglas', 29);
INSERT INTO fatec_user(name, age)
values ('Amanda', 35);
INSERT INTO fatec_user(name, age)
values ('Tadeu', 17);