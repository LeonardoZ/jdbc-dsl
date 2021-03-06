CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
  ID INTEGER NOT NULL,
  NAME VARCHAR(50) NOT NULL,
  PRIMARY KEY (ID));
---
CREATE TABLE IF NOT EXISTS PUBLIC.TODOS (
  ID INTEGER NOT NULL,
  TITLE VARCHAR(100) NOT NULL,
  DESCRIPTION VARCHAR(255) NOT NULL,
  USER_ID INTEGER NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);