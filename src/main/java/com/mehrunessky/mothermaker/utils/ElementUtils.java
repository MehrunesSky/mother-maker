package com.mehrunessky.mothermaker.utils;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@UtilityClass
public class ElementUtils {

    public static boolean fieldIsComplexeClass(TypeMirror type) {
        return type.getKind() == TypeKind.DECLARED && !type.toString().contains("java.");
    }

    public static boolean fieldIsComplexeClass(Element element) {
        var type = element.asType();
        return fieldIsComplexeClass(type);
    }
}
