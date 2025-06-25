package com.mehrunessky.mothermaker.generators;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

@RequiredArgsConstructor(staticName = "of")
public class FieldElementWrapper {
    @Delegate
    private final Element element;

    public TypeElementWrapper getTypeElementWrapper() {
        return TypeElementWrapper.of((TypeElement) ((DeclaredType) element.asType()).asElement());
    }

    public String getFieldName() {
        return element.getSimpleName().toString();
    }
}
