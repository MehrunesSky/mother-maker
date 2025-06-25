package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.utils.StringUtils;
import lombok.experimental.UtilityClass;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

@UtilityClass
public class DataGenerator {

    public static Optional<Object> getData(TypeMirror typeMirror, String name) {
        return switch (typeMirror.getKind()) {
            case INT, SHORT -> Optional.of(0);
            case LONG -> Optional.of(0L);
            case FLOAT -> Optional.of(0.0f);
            case DOUBLE -> Optional.of(0.0d);
            case CHAR -> Optional.of('a');
            case BYTE -> Optional.of((byte) 0);
            case BOOLEAN -> Optional.of(false);
            case DECLARED -> Optional.ofNullable(
                    switch (((DeclaredType) typeMirror).asElement().toString()) {
                        case "java.lang.String" -> StringUtils.addDoubleQuotes(name);
                        case "java.util.List" -> "new java.util.ArrayList<>()";
                        case "java.util.Set" -> "new java.util.HashSet<>()";
                        case "java.util.Map" -> "new java.util.HashMap<>()";
                        default -> null;
                    }
            );
            case VOID, NULL, NONE, ARRAY, ERROR, WILDCARD, PACKAGE, TYPEVAR, EXECUTABLE, OTHER, UNION,
                 INTERSECTION, MODULE -> Optional.empty();
        };
    }
}
