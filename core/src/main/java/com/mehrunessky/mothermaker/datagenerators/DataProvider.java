package com.mehrunessky.mothermaker.datagenerators;

import com.mehrunessky.mothermaker.datagenerators.generators.CollectionGenerator;
import com.mehrunessky.mothermaker.datagenerators.generators.ComplexeClassGenerator;
import com.mehrunessky.mothermaker.datagenerators.generators.DataGenerator;
import com.mehrunessky.mothermaker.datagenerators.generators.EnumGenerator;
import com.mehrunessky.mothermaker.datagenerators.generators.PrimitiveGenerator;
import com.mehrunessky.mothermaker.datagenerators.generators.StringGenerator;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class DataProvider {

    private static final List<DataGenerator> GENERATORS = List.of(
            CollectionGenerator.INSTANCE,
            ComplexeClassGenerator.INSTANCE,
            EnumGenerator.INSTANCE,
            StringGenerator.INSTANCE,
            PrimitiveGenerator.INSTANCE
    );

    public static Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper) {
        return GENERATORS
                .stream()
                .filter(g -> g.test(fieldElementWrapper))
                .flatMap(g -> g.getData(group, fieldElementWrapper).stream())
                .findFirst();
    }
}