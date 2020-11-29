package com.parnikel.ataccama.adapters;

import com.parnikel.ataccama.model.column.Column;
import com.parnikel.ataccama.model.column.ColumnStatistics;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostgresAdapter implements DatabaseAdapter {
    @Override
    @SneakyThrows
    public List<String> getTables(Connection utilSchemaConnection, String schema) {
        PreparedStatement statement = utilSchemaConnection.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema=?");
        statement.setString(1, schema);
        ResultSet rs = statement.executeQuery();
        List<String> tables = new LinkedList<>();
        while (rs.next()) {
            tables.add(rs.getString("table_name"));
        }
        return tables;
    }

    @Override
    @SneakyThrows
    public List<Column> getColumns(Connection utilSchemaConnection, String table, String schema) {
        PreparedStatement statement = utilSchemaConnection.prepareStatement("SELECT column_name, data_type, is_nullable, is_identity FROM information_schema.columns WHERE table_schema=? AND table_name=?");
        statement.setString(1, schema);
        statement.setString(2, table);
        ResultSet rs = statement.executeQuery();
        List<Column> columns = new LinkedList<>();
        while (rs.next()) {
            columns.add(new Column(
                    rs.getString("column_name"),
                    rs.getString("data_type"),
                    rs.getBoolean("is_nullable"),
                    rs.getBoolean("is_identity")
            ));
        }
        return columns;
    }

    @Override
    @SneakyThrows
    public List<String> getColumnNames(Connection utilSchemaConnection, String table, String schema) {
        PreparedStatement statement = utilSchemaConnection.prepareStatement("SELECT column_name FROM information_schema.columns WHERE table_schema=? AND table_name=?");
        statement.setString(1, schema);
        statement.setString(2, table);
        ResultSet rs = statement.executeQuery();
        List<String> columns = new LinkedList<>();
        while (rs.next()) {
            columns.add(rs.getString("column_name"));
        }
        return columns;
    }

    @Override
    @SneakyThrows
    public List<String> getSchemas(Connection utilSchemaConnection) {
        Statement statement = utilSchemaConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT schema_name FROM information_schema.schemata");
        List<String> tables = new LinkedList<>();
        while (rs.next()) {
            tables.add(rs.getString("schema_name"));
        }
        return tables;
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> getDataPreview(Connection connection, String table, List<String> columns) {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " LIMIT 100");
        List<Map<String, Object>> rows = new LinkedList<>();
        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            for (String column : columns) {
                row.put(column, resultSet.getObject(column));
            }
            rows.add(row);
        }
        return rows;
    }

    @Override
    @SneakyThrows
    public List<String> getNumericColumnNames(Connection utilSchemaConnection, String table, String schema) {
        String query = "SELECT column_name " +
                "       FROM information_schema.columns " +
                "       WHERE table_schema=? " +
                "             AND table_name=? " +
                "             AND data_type in ('smallint', 'integer', 'bigint'," +
                "                               'decimal', 'numeric', 'real', 'double precision'," +
                "                               'smallserial', 'serial', 'bigserial', 'money')" +
                "             AND is_identity='NO'";
        PreparedStatement statement = utilSchemaConnection.prepareStatement(query);
        statement.setString(1, schema);
        statement.setString(2, table);
        ResultSet rs = statement.executeQuery();
        List<String> columns = new LinkedList<>();
        while (rs.next()) {
            columns.add(rs.getString("column_name"));
        }
        return columns;
    }

    @Override
    @SneakyThrows
    public List<ColumnStatistics> getStatistics(Connection connection, String tableName, List<String> columns) {
        Statement statement = connection.createStatement();
        String toSelect = columns.stream().map(this::buildColumnStatisticsQuery).collect(Collectors.joining(", "));
        ResultSet rs = statement.executeQuery("SELECT " + toSelect + " FROM " + tableName);
        rs.next();

        List<ColumnStatistics> res = new LinkedList<>();
        for (String column : columns) {
            double avgValue = rs.getDouble("avg_" + column);
            double maxValue = rs.getDouble("max_" + column);
            double minValue = rs.getDouble("min_" + column);
            ColumnStatistics e = new ColumnStatistics(column, avgValue, maxValue, minValue);
            res.add(e);
        }
        return res;
    }

    private String buildColumnStatisticsQuery(String c) {
        return String.format("avg(%1$s) as avg_%1$s, min(%1$s) as min_%1$s, max(%1$s) as max_%1$s", c);
    }
}
