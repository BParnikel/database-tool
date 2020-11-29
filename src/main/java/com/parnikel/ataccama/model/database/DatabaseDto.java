package com.parnikel.ataccama.model.database;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
public class DatabaseDto {
    private String host;
    private int port;
    private String database;
    private String schema;
    private String username;
    private String password;
}
