package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;

import java.util.Optional;

public interface GetData {

    Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper);

    boolean test(FieldElementWrapper fieldElementWrapper);

    int priority();
}
