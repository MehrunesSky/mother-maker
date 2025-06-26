package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import com.mehrunessky.mothermaker.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.lang.model.type.DeclaredType;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataGenerator implements GetData {

    private static final String DEFAULT_STATEMENT = "    .$N($L)\n";

    public static final DataGenerator INSTANCE = new DataGenerator();

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

    public Optional<Tuple> getData(FieldElementWrapper typeElementWrapper) {
        return switch (typeElementWrapper.asType().getKind()) {
            case INT, SHORT -> getTupleWithDefaultStatement(0);
            case LONG -> getTupleWithDefaultStatement(0L);
            case FLOAT -> getTupleWithDefaultStatement(0.0f);
            case DOUBLE -> getTupleWithDefaultStatement(0.0d);
            case CHAR -> getTupleWithDefaultStatement('a');
            case BYTE -> getTupleWithDefaultStatement((byte) 0);
            case BOOLEAN -> getTupleWithDefaultStatement(false);
            case DECLARED -> Optional.ofNullable(
                    switch (((DeclaredType) typeElementWrapper.asType()).asElement().toString()) {
                        case "java.lang.String" -> Tuple.of(
                                DEFAULT_STATEMENT,
                                StringUtils.addDoubleQuotes(typeElementWrapper.getFieldName())
                        );
                        case "java.util.List" -> Tuple.of(DEFAULT_STATEMENT, "new java.util.ArrayList<>()");
                        case "java.util.Set" -> Tuple.of(DEFAULT_STATEMENT, "new java.util.HashSet<>()");
                        case "java.util.Map" -> Tuple.of(DEFAULT_STATEMENT, "new java.util.HashMap<>()");
                        default -> null;
                    }
            );
            case VOID, NULL, NONE, ARRAY, ERROR, WILDCARD, PACKAGE, TYPEVAR, EXECUTABLE, OTHER, UNION,
                 INTERSECTION, MODULE -> Optional.empty();
        };
    }
}
