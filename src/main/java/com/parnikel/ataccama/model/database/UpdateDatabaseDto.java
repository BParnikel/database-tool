package com.parnikel.ataccama.model.database;

import lombok.Data;

@Data
public class UpdateDatabaseDto {
    private String schema;
    private String username;
    private String password;
}
