DROP SCHEMA IF EXISTS itnet CASCADE;

CREATE SCHEMA itnet;

CREATE TABLE itnet.user (
  id VARCHAR(32) PRIMARY KEY,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(32) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  gender VARCHAR(7) NOT NULL DEFAULT 'UNKNOWN',
  birth_date TIMESTAMP NOT NULL,
  address VARCHAR(64) DEFAULT '',
  info VARCHAR(255) DEFAULT ''
  --language VARCHAR(16)
);

CREATE SEQUENCE itnet.friends_seq;

CREATE TABLE itnet.friends (
  id INTEGER PRIMARY KEY DEFAULT nextval('itnet.friends_seq'),
  sender_id VARCHAR(32) NOT NULL,
  receiver_id VARCHAR(32) NOT NULL,
  status VARCHAR(8) NOT NULL DEFAULT 'WAITING',
  FOREIGN KEY (sender_id) REFERENCES itnet.user(id),
  FOREIGN KEY (receiver_id) REFERENCES itnet.user(id)
);

CREATE SEQUENCE itnet.messages_seq;

CREATE TABLE itnet.messages (
  id INTEGER PRIMARY KEY DEFAULT nextval('itnet.messages_seq'),
  sender_id VARCHAR(32) NOT NULL,
  receiver_id VARCHAR(32) NOT NULL,
  text VARCHAR(255) NOT NULL,
  sending_time TIMESTAMP NOT NULL DEFAULT current_timestamp(0),
  status VARCHAR(4) NOT NULL DEFAULT 'SENT',
  FOREIGN KEY (sender_id) REFERENCES itnet.user(id),
  FOREIGN KEY (receiver_id) REFERENCES itnet.user(id)
);