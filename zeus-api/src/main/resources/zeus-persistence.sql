
CREATE TABLE if not exists artifact (
id VARCHAR(36) PRIMARY KEY,
name VARCHAR (100),
path VARCHAR (200),
json_entity TEXT,
created_at datetime,
updated_at datetime
);

create table if not exists tenant (
id VARCHAR(42) PRIMARY KEY,
name VARCHAR (100),
json_entity TEXT,
created_at datetime,
updated_at datetime
);
