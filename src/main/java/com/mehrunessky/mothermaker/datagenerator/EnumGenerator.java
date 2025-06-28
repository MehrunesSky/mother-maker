package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumGenerator implements GetData {

    private static final String DEFAULT_STATEMENT = "    .$N($T.values()[0])\n";

    public static final EnumGenerator INSTANCE = new EnumGenerator();

    @Override
    public Optional<Tuple> getData(FieldElementWrapper fieldElementWrapper) {
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
