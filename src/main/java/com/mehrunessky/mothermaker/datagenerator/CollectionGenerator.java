package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionGenerator implements GetData {

    private static final String DEFAULT_STATEMENT = "    .$N(new $T())\n";

    public static final CollectionGenerator INSTANCE = new CollectionGenerator();

    @Override
    public Optional<Tuple> getData(FieldElementWrapper fieldElementWrapper) {
        return Optional.empty();
    }

    @Override
    public boolean test(FieldElementWrapper fieldElementWrapper) {
        return false;
    }

    @Override
    public int priority() {
        return 0;
    }
}
