package com.mehrunessky.mothermaker.datagenerators.generators;

import com.mehrunessky.mothermaker.datagenerators.Tuple;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;

import java.util.Optional;

public interface DataGenerator {

    Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper);

    boolean test(FieldElementWrapper fieldElementWrapper);

    int priority();
}
