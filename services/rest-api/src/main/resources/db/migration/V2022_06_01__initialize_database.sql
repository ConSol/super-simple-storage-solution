CREATE TABLE public.upload (
    id BIGINT CONSTRAINT upload__pk__id PRIMARY KEY,
    file_name VARCHAR(127) CONSTRAINT upload__not_null__file_name NOT NULL,
    status VARCHAR(31) CONSTRAINT upload__not_null__status NOT NULL,
    content BYTEA,
    total_parts INT
);

CREATE SEQUENCE upload__seq__id INCREMENT BY 1 OWNED BY public.upload.id;

CREATE TABLE public.upload_part(
    id BIGINT CONSTRAINT upload_part__pk__id PRIMARY KEY,
    fk_upload BIGINT REFERENCES public.upload(id) CONSTRAINT upload_part__not_null__fk_upload NOT NULL,
    part_number INT CONSTRAINT upload_part__not_null__part_number NOT NULL,
    content BYTEA CONSTRAINT upload_part__not_null__content NOT NULL,
    CONSTRAINT upload_part__unique__fk_upload__and__part_number  UNIQUE(fk_upload, part_number)
);

CREATE SEQUENCE upload_part__seq__id INCREMENT BY 1 OWNED BY public.upload_part.id;