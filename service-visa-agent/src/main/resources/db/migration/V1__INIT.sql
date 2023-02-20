CREATE SEQUENCE IF NOT EXISTS visa_request_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE visa_request
(
    id      BIGINT NOT NULL,
    user_id VARCHAR(255),
    CONSTRAINT pk_visarequest PRIMARY KEY (id)
);