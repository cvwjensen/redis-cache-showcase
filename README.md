# Welcome to Redis Caching

This branch is the starting point for playing with Redis caching.

There are exercises for the following topics:
- Caching with Redis. See the README-redis-caching.md file for the exercises.
- JPA 2nd level caching with Redis. See the README-jpa-2nd-level-caching.md file for the exercises.

You can safely use this project as a starting point for both topics since they will not interfere with each other.


## Prerequisites

We need a Redis server to play with. This is provided in the docker-compose.yml file.

The project includes a special Spring Boot starter for controlling docker-compose. This is not necessary, but it makes it easier to start the Redis server.

When you start the application, the Redis server will be started automatically.

You can inspect the redis database using the following URL: http://localhost:8001/

First time you must connect to the redis server using the following settings:
- Host: redis
- Port: 6379
- Name: springbootcourse



