package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;

import java.util.Optional;

public sealed interface GetData permits DataGenerator {

    Optional<Tuple> getData(FieldElementWrapper fieldElementWrapper);

    boolean test(FieldElementWrapper fieldElementWrapper);

    int priority();
}
