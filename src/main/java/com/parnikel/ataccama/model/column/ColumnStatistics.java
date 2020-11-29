package com.parnikel.ataccama.model.column;

import lombok.Value;

@Value
public class ColumnStatistics {
    String columnName;
    Double avgValue;
    Double maxValue;
    Double minValue;
}
