package com.mehrunessky.mothermaker.generators;

import com.mehrunessky.mothermaker.Mother;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import java.util.Optional;

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

    public boolean hasDefaultValue() {
        return Optional.ofNullable(element.getAnnotation(Mother.Default.class))
                .isPresent();
    }

    public String getDefaultValue() {
        return Optional.ofNullable(element.getAnnotation(Mother.Default.class))
                .map(Mother.Default::value).orElse(null);
    }

    public boolean isDeclaredType() {
        return this.asType().getKind() == TypeKind.DECLARED;
    }

    public boolean isStringType() {
        return isDeclaredType() && "java.lang.String"
                .equals(this.getTypeElementWrapper().getTypeElement().toString());
    }
}
