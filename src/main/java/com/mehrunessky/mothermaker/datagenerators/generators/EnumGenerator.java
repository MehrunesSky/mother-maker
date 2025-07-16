package com.mehrunessky.mothermaker.datagenerators.generators;

import com.mehrunessky.mothermaker.datagenerators.Tuple;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumGenerator implements DataGenerator {

    private static final String DEFAULT_STATEMENT = "$T.values()[0]";

    public static final EnumGenerator INSTANCE = new EnumGenerator();

    @Override
    public Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper) {
        String enumValue = fieldElementWrapper.getValueForGroupOrDefault(group);
        if (enumValue != null) {
            return Optional.of(
                    Tuple.of(
                            "$T.$N",
                            fieldElementWrapper.getTypeElementWrapper().getClassName(),
                            enumValue
                    )
            );
        }
        return Optional.of(
                Tuple.of(
                        DEFAULT_STATEMENT,
                        fieldElementWrapper.getTypeElementWrapper().getClassName()
                )
        );
    }

    @Override
    public boolean test(FieldElementWrapper fieldElementWrapper) {
        return fieldElementWrapper.isDeclaredType() &&
                fieldElementWrapper.getTypeElementWrapper().isEnum();
    }

    @Override
    public int priority() {
        return 0;
    }
}
