package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionGenerator implements GetData {

    private static final String DEFAULT_STATEMENT = "    .$N(new $T())\n";

    public static final CollectionGenerator INSTANCE = new CollectionGenerator();

    @Override
    public Optional<Tuple> getData(FieldElementWrapper fieldElementWrapper) {
        return Optional.ofNullable(
                switch (fieldElementWrapper.getTypeElementWrapper().getTypeElement().toString()) {
                    case "java.util.List" -> Tuple.of(DEFAULT_STATEMENT, ClassName.get(ArrayList.class));
                    case "java.util.Set" -> Tuple.of(DEFAULT_STATEMENT, ClassName.get(HashSet.class));
                    case "java.util.Map" -> Tuple.of(DEFAULT_STATEMENT, ClassName.get(HashMap.class));
                    default -> null;
                }
        );
    }

    @Override
    public boolean test(FieldElementWrapper fieldElementWrapper) {
        return fieldElementWrapper.isCollectionType();
    }

    @Override
    public int priority() {
        return 0;
    }
}
