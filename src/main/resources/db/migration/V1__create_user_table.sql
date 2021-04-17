CREATE TABLE IF NOT EXISTS fatec_user (
    id int8 NOT NULL,
    name varchar(255) NOT NULL,
    age integer NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO fatec_user(id, name, age) values (1,  'Douglas', 29);
INSERT INTO fatec_user(id, name, age) values (2,  'Amanda', 35);
INSERT INTO fatec_user(id, name, age) values (3,  'Tadeu', 17);