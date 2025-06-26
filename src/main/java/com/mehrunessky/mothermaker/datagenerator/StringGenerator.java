package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringGenerator implements GetData {

    private static final String DEFAULT_STATEMENT = "    .$N($S)\n";

    public static final StringGenerator INSTANCE = new StringGenerator();

    @Override
    public Optional<Tuple> getData(FieldElementWrapper fieldElementWrapper) {
        String value;
        if (fieldElementWrapper.hasDefaultValue()) {
            value = fieldElementWrapper.getDefaultValue();
        } else {
            value = fieldElementWrapper.getFieldName();
        }
        return Optional.of(Tuple.of(DEFAULT_STATEMENT, value));
    }

    @Override
    public boolean test(FieldElementWrapper fieldElementWrapper) {
        return fieldElementWrapper.isStringType();
    }

    @Override
    public int priority() {
        return 0;
    }
}
