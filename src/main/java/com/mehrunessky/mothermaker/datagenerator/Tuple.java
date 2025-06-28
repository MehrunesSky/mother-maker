package com.mehrunessky.mothermaker.datagenerator;

public record Tuple(String statement, Object[] object) {

    static Tuple of(String statement, Object... object) {
        return new Tuple(statement, object);
    }
}
