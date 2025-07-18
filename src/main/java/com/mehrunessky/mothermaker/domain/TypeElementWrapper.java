package com.mehrunessky.mothermaker.domain;

import com.mehrunessky.mothermaker.utils.GetFields;
import com.squareup.javapoet.ClassName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "of")
public class TypeElementWrapper {
    @Delegate
    @Getter
    private final TypeElement typeElement;

    public ClassName getClassName() {
        return ClassName.get((TypeElement) ((DeclaredType) typeElement.asType()).asElement());
    }

    public ClassName getClassNameWithNameModification(Function<String, String> f) {
        var c = getClassName();
        return ClassName.get(c.packageName(), f.apply(c.simpleName()));
    }

    public List<FieldElementWrapper> getPrimitiveFields() {
        return GetFields.of(typeElement)
                .withWithoutSubClasses(true)
                .getFields()
                .stream()
                .map(FieldElementWrapper::of)
                .toList();
    }

    public List<FieldElementWrapper> getComplexFields() {
        return GetFields.of(typeElement)
                .withOnlySubClasses(true)
                .getFields()
                .stream()
                .map(FieldElementWrapper::of)
                .toList();
    }

    public List<FieldElementWrapper> getFields() {
        return GetFields.of(typeElement)
                .getFields()
                .stream()
                .map(FieldElementWrapper::of)
                .toList();
    }

    public ClassName getLombokBuilderClassName() {
        return getClassName().nestedClass(getClassName().simpleName() + "Builder");
    }

    public boolean isEnum() {
        return typeElement.getKind() == ElementKind.ENUM;
    }

    public ClassName getMotherClassName() {
        return getClassNameWithNameModification(s -> s + "Mother");
    }

    public boolean containSetter(String setterName) {
        return this.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .anyMatch(m -> m.getSimpleName().toString().equals(setterName));
    }
}
