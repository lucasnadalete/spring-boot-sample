CREATE TABLE IF NOT EXISTS revision_pdf_pages
(
    id          serial8      NOT NULL,
    pdf_page_id int8         NOT NULL,
    revision    varchar(10)  NOT NULL,
    file_name   varchar(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_revision_pdf_pages
        FOREIGN KEY (pdf_page_id)
            REFERENCES pdf_pages_data (id),
    UNIQUE (pdf_page_id, revision)
);

INSERT INTO revision_pdf_pages(pdf_page_id, revision, file_name)
values (1, 'REVISION 1', '1_1_3.pdf');