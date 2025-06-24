package com.mehrunessky.mothermaker.generators;

import com.mehrunessky.mothermaker.utils.GetFields;
import com.squareup.javapoet.ClassName;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "of")
public class TypeElementWrapper {
    @Delegate
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
                .withOnlyWithSetter(true)
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

    public ClassName getLombokBuilderClassName() {
        return getClassName().nestedClass(getClassName().simpleName() + "Builder");
    }

    public ClassName getMotherClassName() {
        return getClassNameWithNameModification(s -> s + "Mother");
    }
}
