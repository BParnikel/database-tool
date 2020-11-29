package com.parnikel.ataccama.adapters;

import com.parnikel.ataccama.model.column.Column;
import com.parnikel.ataccama.model.column.ColumnStatistics;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface DatabaseAdapter {
    List<String> getTables(Connection connection, String schema);

    List<Column> getColumns(Connection connection, String table, String schema);

    List<String> getColumnNames(Connection utilSchemaConnection, String table, String schema);

    List<String> getSchemas(Connection utilSchemaConnection);

    List<Map<String, Object>> getDataPreview(Connection utilSchemaConnection, String table, List<String> columns);

    List<String> getNumericColumnNames(Connection utilSchemaConnection, String tableName, String schema);

    List<ColumnStatistics> getStatistics(Connection connection, String tableName, List<String> columns);
}
