\connect springsecurity

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users
(
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  username text not null unique,
  password text not null,
		active boolean not null DEFAULT	false,
		roles text not null 
);

ALTER TABLE "users" OWNER TO testuser;

insert into users (username, password, active, roles) values ('someuser','somepassword',true,'ROLE_USER');
insert into users (username, password, active, roles) values ('someadmin','somepassword',true,'ROLE_ADMIN');
