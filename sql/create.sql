CREATE TABLE employee (
    id varchar(10) PRIMARY KEY,
    first_name varchar(100),
    last_name varchar(100)
);

INSERT INTO employee (
  id,
  first_name,
  last_name
) VALUES (
  '0000000001',
  'Taro',
  'Yamada'
);

INSERT INTO employee (
  id,
  first_name,
  last_name
) VALUES (
  '0000000002',
  'Jiro',
  'Yamada'
);
