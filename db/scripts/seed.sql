\connect apidb

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

  -- id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
CREATE TABLE users
(
  id integer PRIMARY KEY,
  username text not null unique,
  password text not null,
		active boolean not null DEFAULT	false,
		roles text not null 
);

-- ALTER TABLE "users" OWNER TO testuser;

insert into users (id, username, password, active, roles) values (1, 'someuser','somepassword',true,'ROLE_USER');
insert into users (id, username, password, active, roles) values (2, 'someadmin','somepassword',true,'ROLE_ADMIN');
