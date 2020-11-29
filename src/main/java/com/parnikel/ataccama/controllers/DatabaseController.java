package com.parnikel.ataccama.controllers;

import com.parnikel.ataccama.dao.PostgresDatabaseRepository;
import com.parnikel.ataccama.model.column.Column;
import com.parnikel.ataccama.model.column.ColumnStatistics;
import com.parnikel.ataccama.model.database.Database;
import com.parnikel.ataccama.model.database.DatabaseDto;
import com.parnikel.ataccama.model.database.UpdateDatabaseDto;
import com.parnikel.ataccama.services.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/databases")
@RequiredArgsConstructor
public class DatabaseController {
    private final PostgresDatabaseRepository postgresDatabaseRepository;
    private final DatabaseService databaseService;

    @GetMapping
    public List<Database> getDatabases() {
        return postgresDatabaseRepository.findAll();
    }

    @PostMapping
    public Database addDatabase(@RequestBody DatabaseDto databaseDto) {
        return databaseService.addDatabase(databaseDto.getHost(), databaseDto.getPort(),
                databaseDto.getDatabase(), databaseDto.getSchema(), databaseDto.getUsername(), databaseDto.getPassword());
    }

    @PutMapping("/{databaseId}")
    public Database updateDatabase(@PathVariable("databaseId") long databaseId,
                                   @RequestBody UpdateDatabaseDto databaseDto) {
        return databaseService.updateDatabase(databaseId, databaseDto.getSchema(), databaseDto.getUsername(), databaseDto.getPassword());
    }

    @DeleteMapping("/{databaseId}")
    public void deleteDatabase(@PathVariable("databaseId") long databaseId) {
        databaseService.deleteDatabase(databaseId);
    }

    @GetMapping("/{databaseId}/schemas")
    public List<String> getSchemas(@PathVariable("databaseId") long databaseId) {
        return databaseService.getSchemas(databaseId);
    }

    @GetMapping("/{databaseId}/tables")
    public List<String> getTables(@PathVariable("databaseId") long databaseId) {
        return databaseService.getTables(databaseId);
    }

    @GetMapping("/{databaseId}/columns")
    public List<Column> getColumns(@PathVariable("databaseId") long databaseId,
                                   @RequestParam("tableName") String tableName) {
        return databaseService.getColumns(databaseId, tableName);
    }

    @GetMapping("/{databaseId}/data-preview")
    public List<Map<String, Object>> getData(@PathVariable("databaseId") long databaseId,
                                             @RequestParam("tableName") String tableName) {
        return databaseService.getDataPreview(databaseId, tableName);
    }

    @GetMapping("/{databaseId}/statistics")
    public List<ColumnStatistics> getTableStatistics(@PathVariable("databaseId") long databaseId,
                                                     @RequestParam("tableName") String tableName) {
        return databaseService.getTableStatistics(databaseId, tableName);
    }
}
