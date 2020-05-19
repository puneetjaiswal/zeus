# zeus

Start mysql
```xml
docker run -d -p 3306:3306  --name mysqldb -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=zeus -d mysql:5.7
```
Run DDL after connecting to mysql
`mysql -uroot -proot123 -h127.0.0.1 -Dzeus`

```
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
```

Build and Run
```
mvn clean install
cd zeus-api/target
java -jar zeus-api-1.0.0-jar-with-* server ../config/zeus-local.yaml
```

Populate entities
```
00:17 $ curl -H "Content-Type: application/json" -X POST http://localhost:8080/entity?entityType=TENANT  -d '{  "name": "tenant1Test" }'
```

Get all entities
```
curl -X GET http://localhost:8080/entity/TENANT
```

View the enties here
```
http://localhost:8080/entity-view
```
