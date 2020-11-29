package com.parnikel.ataccama.services;

import com.parnikel.ataccama.adapters.DatabaseAdapter;
import com.parnikel.ataccama.connection.ConnectionPool;
import com.parnikel.ataccama.dao.PostgresDatabaseRepository;
import com.parnikel.ataccama.exceptions.DbConnectionException;
import com.parnikel.ataccama.exceptions.NoDatabaseForId;
import com.parnikel.ataccama.model.column.Column;
import com.parnikel.ataccama.model.column.ColumnStatistics;
import com.parnikel.ataccama.model.database.Database;
import com.parnikel.ataccama.model.database.DatabaseFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class DatabaseService {
    private final ConnectionPool connectionPool;
    private final DatabaseFactory databaseFactory;
    private final DatabaseAdapter databaseAdapter;
    private final PostgresDatabaseRepository postgresDatabaseRepository;

    @SneakyThrows
    public Database addDatabase(String host, int port, String database, String schema, String user, String password) {
        Database db = databaseFactory.makeDatabase(host, port, database, schema, user, password);
        connect(db);
        return postgresDatabaseRepository.save(db);
    }

    public Database updateDatabase(long databaseId, String schema, String username, String password) {
        Database db = getDatabase(databaseId);
        if (Objects.equals(db.getSchema(), schema) && Objects.equals(db.getUsername(), username) && Objects.equals(db.getPassword(), password)) {
            return db;
        }
        db.setSchema(schema);
        db.setUsername(username);
        db.setPassword(password);
        connect(db);
        return postgresDatabaseRepository.save(db);
    }

    public void deleteDatabase(long databaseId) {
        postgresDatabaseRepository.deleteById(databaseId);
    }

    public List<String> getSchemas(long databaseId) {
        Database database = getDatabase(databaseId);
        Connection connection = getUtilSchemaConnection(database);
        return databaseAdapter.getSchemas(connection);
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<String> getTables(long databaseId) {
        Database database = getDatabase(databaseId);
        Connection utilSchemaConnection = getUtilSchemaConnection(database);
        return databaseAdapter.getTables(utilSchemaConnection, database.getSchema());
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<Column> getColumns(long databaseId, String table) {
        Database database = getDatabase(databaseId);
        Connection utilSchemaConnection = getUtilSchemaConnection(database);
        return databaseAdapter.getColumns(utilSchemaConnection, table, database.getSchema());
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDataPreview(long databaseId, String tableName) {
        Database database = getDatabase(databaseId);
        Connection utilSchemaConnection = getUtilSchemaConnection(database);
        Connection connection = getSchemaConnection(database);
        List<String> columns = databaseAdapter.getColumnNames(utilSchemaConnection, tableName, database.getSchema());
        return databaseAdapter.getDataPreview(connection, tableName, columns);
    }

    @Transactional(readOnly = true)
    public List<ColumnStatistics> getTableStatistics(long databaseId, String tableName) {
        Database database = getDatabase(databaseId);
        Connection utilSchemaConnection = getUtilSchemaConnection(database);
        Connection connection = getSchemaConnection(database);
        List<String> columns = databaseAdapter.getNumericColumnNames(utilSchemaConnection, tableName, database.getSchema());
        return databaseAdapter.getStatistics(connection, tableName, columns);
    }

    private Connection getUtilSchemaConnection(Database database) {
        Database utilDatabase = databaseFactory.makeUtilDatabase(database);
        return getSchemaConnection(utilDatabase);
    }

    private Connection getSchemaConnection(Database database) {
        Connection connection = connectionPool.getConnection(database);
        if (connection == null) {
            connection = connect(database);
        }
        return connection;
    }

    @SneakyThrows
    private Database getDatabase(long databaseId) {
        return postgresDatabaseRepository.findById(databaseId)
                .orElseThrow(() -> new NoDatabaseForId("No database found for id: " + databaseId));
    }

    @SneakyThrows
    private synchronized Connection connect(Database database) {
        try {
            Connection connection = DriverManager.getConnection(database.getConnectionUrl(), database.getUsername(), database.getPassword());
            connectionPool.addConnection(database, connection);
            return connection;
        } catch (SQLException e) {
            throw new DbConnectionException(e);
        }
    }
}
