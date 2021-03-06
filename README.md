# JDBC-DSL

## BE CAREFUL -  JDBC-DSL it's not production ready yet. 

[![Build Status](https://travis-ci.org/LeonardoZ/jdbc-dsl.svg?branch=master)](https://travis-ci.org/LeonardoZ/jdbc-dsl)
[![Coverage Status](https://coveralls.io/repos/github/LeonardoZ/jdbc-dsl/badge.svg)](https://coveralls.io/github/LeonardoZ/jdbc-dsl)

JDBC-DSL is a simple Domain Specific Language that allows a flexible manipulation of the JDBC API.
It supports:
  - DML (Data Modification Language - Insert/Update/Delete);
  - DQL (Data Query Language - Select);
  - Batch operations;
  - Transactional operations;

# Maven
```xml
<dependency>
  <groupId>br.com.leonardoz</groupId>
  <artifactId>jdbc-dsl</artifactId>
  <version>0.2-BETA</version>
</dependency>
```

# Usage
First, you need to implement the br.com.leonardoz.dsl.ConnectionFactory interface. This will be used by the DSL for every subsequential operation.

```java
public interface ConnectionFactory {
   Connection getConnection();
    
   Connection getTransactionalConnection();
}
```
We recommend you to use an Connection Pool like HikariCP to maintain the efficiency.

# DML
Using DML statements (like INSERT, DELETE or UPDATE):
```java
    User newUser = createUser();
    ConnectionFactory factory = factory();
        JdbcDsl.sql(factory)
            .statement("INSERT INTO USERS VALUES (?, ?)")
            .withParameters(user.getId(), user.getName())
            .asDml()
            .execute();
```

# Query
For using Queries, you will need to implement a br.com.leonardoz.dsl.query.ResultSetToEntity<T> to parse from a ResultSet to some Entity in your project. 
```java
    ResultSetToEntity<User> userParser = new ResultSetToEntity<User>() {
        @Override
        public User parse(ResultSet resultSet) throws SQLException {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            User user = new User();
            user.setId(id);
            user.setName(name);
            return user;
        }
    };
```
or with the Java 8 syntax:
```java
    ResultSetToEntity<User> userParser = (resultSet) -> {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    };
```

And to query with the DSL:
```java
    ConnectionFactory factory = factory();
    // Returning multiple rows
    List<User> selectedUsers = JdbcDsl
        .sql(factory)
        .statement("SELECT ID, NAME FROM USERS")
        .asQuery(userParser)
        .getAll();
        
    // Returning a single row
    User selectedUser = JdbcDsl
        .sql(factory)
        .statement("SELECT ID, NAME FROM USERS WHERE ID = ?")
        .withParameters(2)
        .asQuery(userParser)
        .get();
```

# Batch
JDBC supports Batch operations. In JDBC-DSL we built a fluent API for doing Batch operations (that can also be broken to use something like for-each loops), with the ideia of making flexible to use UPDATE or INSERT a certain amount of data
```java
    ConnectionFactory factory = factory();
    User user1, user2, user3 = ...;
    int[] newRows = JdbcDsl
          .batch(factory)
          .statement("INSERT INTO USERS VALUES (?, ?)")
          .addEntry(user1.getId(), user1.getName())
          .addEntry(user2.getId(), user2.getName())
          .addEntry(user3.getId(), user3.getName())
          .build()
          .execute();
    // Breaking the Fluent API
    List<User> users = ...;
    BatchStatementBuilder statement = JdbcDsl
        .batch(factory)
        .statement("INSERT INTO USERS VALUES (?, ?)");
        for (User user : users) {
            statement.addEntry(user.getId(), user.getName());
        }
        int[] reallyNewRows = statement
            .build()
            .execute()
```

# Transactions
A transaction is a atomic operation. To use Transactions in JDBC-DSL you need to follow a imposed structure by the JdbcApi, as the example shows:
```java
    JdbcDsl.transaction(factory)
        .doInTransaction(new TransactionArea() {
            @Override
            public void interactWithDatabase(Connection connection, JdbcTransactionalDsl dsl) throws SQLException {
                dsl.dml("INSERT INTO USERS VALUES(?, ?)", 62, "RICK");
                dsl.dml("INSERT INTO USERS VALUES(?, ?)", 3, "ANDY");
                dsl.dml("INSERT INTO USERS VALUES(?, ?)", 14, "MORTY");
            }
    });
```
or using the Java 8 syntax:
```java
    JdbcDsl.transaction(factory)
        .doInTransaction((connection, dsl) -> {
            dsl.dml("INSERT INTO USERS VALUES(?, ?)", 62, "RICK");
            dsl.dml("INSERT INTO USERS VALUES(?, ?)", 3, "ANDY");
            dsl.dml("INSERT INTO USERS VALUES(?, ?)", 14, "MORTY");
        });
```

# Logging
(soon)

# Tests
(soon)



License
----

MIT


**Free Software, Hell Yeah!**

