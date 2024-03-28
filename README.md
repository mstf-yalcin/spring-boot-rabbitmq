# Spring Boot With RabbitMQ


<ul>
  <li>RabbitMQ</li>
  <li>Swagger</li>
</ul>

docker-compose.yml
```yml
services:
  producer:
    container_name: spring-producer
    build:
      context: ./producer
      dockerfile: Dockerfile
  consumer:
    build:
      context: ./consumer
      dockerfile: Dockerfile
    deploy:
      replicas: 2
  rabbitmq-server:
    image: rabbitmq:3-management

```
docker-override.compose.yml
```yml

services:
  producer:
    ports:
      - 80:8080
    environment:
      - SPRING_PROFILES_ACTIVE=compose
      - RABBITMQ_HOST=rabbitmq-server
      - RABBITMQ_USER=user
      - RABBITMQ_PASS=password
      - RABBITMQ_PORT=5672
    depends_on:
      - rabbitmq-server
    restart: always
  consumer:
    environment:
      - SPRING_PROFILES_ACTIVE=compose
      - RABBITMQ_HOST=rabbitmq-server
      - RABBITMQ_USER=user
      - RABBITMQ_PASS=password
      - RABBITMQ_PORT=5672
    depends_on:
      - rabbitmq-server
      - producer
    restart: always
  rabbitmq-server:
    environment:
      - RABBITMQ_DEFAULT_USER=user
      - RABBITMQ_DEFAULT_PASS=password
    ports:
      - 5672:5672
      - 15672:15672
    restart: always

```



 Will create one RabbitMQ server, one producer and two consumer containers in the project.
```bash
docker compose up -d
```

For RabbitMQ Management ui user: user password: password
```docker
localhost:15672
```

For Producer Swagger ui
```docker
localhost:80/swagger-ui.html
```

For viewing logs of Consumer-1
```bash
 docker logs -f spring-boot-rabbitmq-consumer-1
```

For viewing logs of Consumer-2
```docker
 docker logs -f spring-boot-rabbitmq-consumer-2
```
