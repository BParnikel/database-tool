package com.parnikel.ataccama.connection;

import com.parnikel.ataccama.model.database.Database;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConnectionPool {
    private final Map<Database, Connection> connectionMap = new HashMap<>();

    @PreDestroy
    @SneakyThrows
    public void cleanUp() {
        for (Connection conn : connectionMap.values()) {
            conn.close();
        }
    }

    public Connection getConnection(Database database) {
        return connectionMap.get(database);
    }

    @SneakyThrows
    public void addConnection(Database database, Connection connection) {
        Connection previousConnection = connectionMap.get(database);
        if (previousConnection != null) { // e.g. in case of credentials update
            previousConnection.close();
        }
        connectionMap.put(database, connection);
    }
}
