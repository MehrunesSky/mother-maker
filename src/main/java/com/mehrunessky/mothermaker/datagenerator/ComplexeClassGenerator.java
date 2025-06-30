package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import com.mehrunessky.mothermaker.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComplexeClassGenerator implements GetData {

    public static final String DEFAULT_STATEMENT = "    .$N($N.build())\n";

    public static final ComplexeClassGenerator INSTANCE = new ComplexeClassGenerator();

    @Override
    public Optional<Tuple> getData(String group, FieldElementWrapper fieldElementWrapper) {
        var method =
                Optional.ofNullable(fieldElementWrapper.getValueForGroup(group)).orElse("create");
        return Optional.of(
                Tuple.of(
                        DEFAULT_STATEMENT,
                        fieldElementWrapper.getSimpleName().toString() + StringUtils.capitalize(method)
                )
        );
    }

    @Override
    public boolean test(FieldElementWrapper fieldElementWrapper) {
        return fieldElementWrapper.isDeclaredType() &&
                !fieldElementWrapper.getTypeElementWrapper().isEnum() &&
                !fieldElementWrapper.getTypeElementWrapper().getTypeElement().toString().contains("java.");
    }

    @Override
    public int priority() {
        return 0;
    }
}
