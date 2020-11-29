package com.parnikel.ataccama.model.column;

import lombok.Value;

@Value
public class Column {
    String name;
    String type;
    boolean nullable;
    boolean identity;
}
