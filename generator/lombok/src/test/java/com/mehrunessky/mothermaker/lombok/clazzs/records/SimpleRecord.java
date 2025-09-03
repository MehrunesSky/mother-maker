package com.mehrunessky.mothermaker.lombok.clazzs.records;

import com.mehrunessky.mothermaker.Mother;
import lombok.Builder;

@Mother
@Builder
public record SimpleRecord(
    @Mother.Default("Default Name") String name,
    @Mother.Default("25") int age,
    @Mother.Default("true") boolean active,
    String description
) {
}