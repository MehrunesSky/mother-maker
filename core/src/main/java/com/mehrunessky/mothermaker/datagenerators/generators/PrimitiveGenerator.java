package com.mehrunessky.mothermaker.datagenerators.generators;

import com.mehrunessky.mothermaker.datagenerators.Tuple;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrimitiveGenerator implements DataGenerator {

    private static final String DEFAULT_STATEMENT = "$L";

    public static final PrimitiveGenerator INSTANCE = new PrimitiveGenerator();

    @Override
    public boolean test(FieldElementWrapper fieldElementWrapper) {
        return true;
    }

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }

    private static Optional<Tuple> getTupleWithDefaultStatement(Object o) {
        return Optional.of(Tuple.of(DEFAULT_STATEMENT, o));
    }

    public Optional<Tuple> getData(String group, FieldElementWrapper typeElementWrapper) {
        var defaultValue = typeElementWrapper.getValueForGroupOrDefault(group);
        if (defaultValue != null) {
            return getTupleWithDefaultStatement(defaultValue);
        }
        return switch (typeElementWrapper.asType().getKind()) {
            case INT, SHORT -> getTupleWithDefaultStatement(0);
            case LONG -> getTupleWithDefaultStatement(0L);
            case FLOAT -> getTupleWithDefaultStatement(0.0f);
            case DOUBLE -> getTupleWithDefaultStatement(0.0d);
            case CHAR -> getTupleWithDefaultStatement('a');
            case BYTE -> getTupleWithDefaultStatement((byte) 0);
            case BOOLEAN -> getTupleWithDefaultStatement(false);
            case DECLARED, VOID, NULL, NONE, ARRAY, ERROR, WILDCARD, PACKAGE, TYPEVAR, EXECUTABLE, OTHER, UNION,
                 INTERSECTION, MODULE -> Optional.empty();
        };
    }
}
