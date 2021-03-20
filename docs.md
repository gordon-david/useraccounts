# Spring Security Demo Project

A showcase of potential configuration of an authorization service using Spring Security.

## Installation and Demo



## building process

1. java api: requires packaging a jar file that is copied to the docker container.
	- package jar: `./mvnw package`
	- build container: `podman build ./java-api`
2. database:
	- `podman build ./database/`
3. docker-compose: build, up, etc. as normal.
	- ex: `sudo docker-compose up`

## Api Endpoints

### Test Resource Endpoints

#### GET /admintestresource
#### GET /usertestresource
#### GET /hometestresource

### Authentication Endpoints

#### POST /users/authenticate
#### DELETE /users/authenticate