# User Account System Demo Project

A showcase of potential configuration of an authorization service using Spring Security.

## Installation and Demo

## building process

The project can be built together using Docker Compose: `sudo docker-compose build` or separately using docker `docker build ./authservice-db && docker build ./authservice-api`. Next, the project can be run via `docker-compose up`.

## Api Endpoints

### Test Resource Endpoints

#### GET /admintestresource

#### GET /usertestresource

#### GET /hometestresource

### Authentication Endpoints

#### POST /users/authenticate

#### DELETE /users/authenticate

### User Account Endponts

#### POST /users

#### GET /users

#### GET /users/{id}

#### PUT /users/{id}

#### PATCH /users/{id}

#### DELETE /users/{id}
