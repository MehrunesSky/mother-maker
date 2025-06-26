package com.mehrunessky.mothermaker.datagenerator;

public record Tuple(String statement, Object object) {

    static Tuple of(String statement, Object Object) {
        return new Tuple(statement, Object);
    }
}
