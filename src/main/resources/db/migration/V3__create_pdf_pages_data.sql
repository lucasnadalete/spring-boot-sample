CREATE TABLE IF NOT EXISTS pdf_pages_data
(
    id    serial8     NOT NULL,
    block varchar(10) NOT NULL,
    code  varchar(10) NOT NULL,
    page  int8        NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (block, code, page)
);

INSERT INTO pdf_pages_data(block, code, page)
values ('BLOCK 1', 'CODE 1', 1);

INSERT INTO pdf_pages_data(block, code, page)
values ('BLOCK 2', 'CODE 1', 3);