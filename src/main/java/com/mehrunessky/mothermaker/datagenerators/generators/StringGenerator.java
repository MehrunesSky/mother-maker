package com.mehrunessky.mothermaker.datagenerators.generators;

import com.mehrunessky.mothermaker.datagenerators.Tuple;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringGenerator implements DataGenerator {

    private static final String DEFAULT_STATEMENT = "$S";

    public static final StringGenerator INSTANCE = new StringGenerator();

    @Override
    public Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper) {
        String value = Optional
                .ofNullable(fieldElementWrapper.getValueForGroup(group))
                .or(() -> Optional.ofNullable(fieldElementWrapper.getDefaultValue()))
                .orElse(fieldElementWrapper.getFieldName());
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
