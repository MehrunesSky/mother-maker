package com.mehrunessky.mothermaker.utils;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public class ClassElementUtils {

    public static boolean classContainSetter(TypeElement classElement, String setterName) {
        return classElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .anyMatch(m -> m.getSimpleName().toString().equals(setterName));
    }
}
