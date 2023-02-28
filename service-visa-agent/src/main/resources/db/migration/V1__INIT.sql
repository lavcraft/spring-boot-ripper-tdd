CREATE SEQUENCE IF NOT EXISTS visa_request_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE visa_request
(
    id      BIGINT NOT NULL,
    user_id VARCHAR(255),
    status VARCHAR(255),
    CONSTRAINT pk_visarequest PRIMARY KEY (id)
);

CREATE FUNCTION AddData() RETURNS INTEGER
AS
$$
BEGIN
    INSERT INTO visa_request (id, user_id) VALUES (1, 'user0' + random());
    RETURN 1;
END;
$$ LANGUAGE plpgsql;
