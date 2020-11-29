package com.parnikel.ataccama.model.database;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseFactory {
    public Database makeDatabase(String host, int port, String database, String schema, String user, String password) {
        return new Database(host, port, database, schema, user, password);
    }

    @SneakyThrows
    public Database makeUtilDatabase(Database database) {
        return makeDatabase(database.getHost(), database.getPort(),
                database.getDatabase(), "information_schema", database.getUsername(), database.getPassword());
    }
}
