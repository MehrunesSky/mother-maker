package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class DataProvider {

    private static final List<GetData> GENERATORS = List.of(
            CollectionGenerator.INSTANCE,
            ComplexeClassGenerator.INSTANCE,
            EnumGenerator.INSTANCE,
            StringGenerator.INSTANCE,
            DataGenerator.INSTANCE
    );

    public static Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper) {
        return GENERATORS
                .stream()
                .filter(g -> g.test(fieldElementWrapper))
                .flatMap(g -> g.getData(group, fieldElementWrapper).stream())
                .findFirst();
    }
}