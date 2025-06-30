package com.mehrunessky.mothermaker.datagenerators;

public record Tuple(String statement, Object[] object) {

    public static Tuple of(String statement, Object... object) {
        return new Tuple(statement, object);
    }
}
