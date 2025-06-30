package com.mehrunessky.mothermaker.datagenerators.generators;

import com.mehrunessky.mothermaker.datagenerators.Tuple;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumGenerator implements DataGenerator {

    private static final String DEFAULT_STATEMENT = "    .$N($T.values()[0])\n";

    public static final EnumGenerator INSTANCE = new EnumGenerator();

    @Override
    public Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper) {
        if (fieldElementWrapper.hasDefaultValue()) {
            return Optional.of(
                    Tuple.of(
                            "    .$N($T.$N)\n",
                            fieldElementWrapper.getTypeElementWrapper().getClassName(),
                            fieldElementWrapper.getDefaultValue()
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
