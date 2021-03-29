# User Account System Demo Project

A showcase of potential configuration of an authorization service using Spring Security.

## Installation and Demo

## Building and running the development and testing environment

The project currently uses three modules, a client app that can be served statically, a Spring api, and a postgres database. My recommended testing environment involves serving the database in a docker container -- it's docker file is found under `authservice-db/Dockerfile`. The Spring api can be served via an included Maven plugin. The client app, which simply includes some forms for testing against the api, can be served using either yarn or npm (`yarn serve` or `npm run serve` respectively).

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
