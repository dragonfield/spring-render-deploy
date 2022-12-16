CREATE TABLE employee (
    id varchar(10) PRIMARY KEY,
    first_name varchar(100),
    last_name varchar(100)
);

CREATE SEQUENCE IF NOT EXISTS EMPLOYEE_ID_SEQ
    INCREMENT BY 1
    MAXVALUE 9999999999
    MINVALUE 1
    START WITH 1
;
