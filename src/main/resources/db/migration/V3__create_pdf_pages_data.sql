CREATE TABLE IF NOT EXISTS pdf_pages_data
(
    id    serial8     NOT NULL,
    block varchar(50) NOT NULL,
    code  varchar(50) NOT NULL,
    page  int8        NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (block, code, page)
);