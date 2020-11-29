package com.parnikel.ataccama.model.database;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = {"host", "port", "database", "schema"})
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    protected String host;
    protected int port;
    protected String database;
    protected String schema;
    protected String username;
    protected String password;

    public Database() {
    }

    public Database(String host, int port, String database, String schema, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.schema = schema;
        this.username = username;
        this.password = password;
    }

    public String getConnectionUrl() {
        return "jdbc:postgresql://" + host + "/" + database + "?currentSchema=" + schema;
    }
}
