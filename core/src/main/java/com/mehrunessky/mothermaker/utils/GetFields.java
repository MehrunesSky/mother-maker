package com.mehrunessky.mothermaker.utils;

import com.mehrunessky.mothermaker.Mother;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.With;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

import static com.mehrunessky.mothermaker.utils.ElementUtils.fieldIsComplexeClass;
import static com.mehrunessky.mothermaker.utils.ElementUtils.fieldIsEnum;
import static com.mehrunessky.mothermaker.utils.StringUtils.capitalize;

@Builder
@RequiredArgsConstructor
public class GetFields {

    @NonNull
    private final TypeElementWrapper typeElement;

    @Builder.Default
    @With
    private final boolean onlyWithSetter = false;

    @Builder.Default
    @With
    private final boolean onlySubClasses = false;

    @Builder.Default
    @With
    private final boolean withoutSubClasses = false;

    public static GetFields of(TypeElementWrapper typeElement) {
        return GetFields
                .builder()
                .typeElement(typeElement)
                .build();
    }

    public static GetFields of(TypeElement typeElement) {
        return GetFields
                .builder()
                .typeElement(TypeElementWrapper.of(typeElement))
                .build();
    }

    private static boolean isPrivate(Element element) {
        return element.getModifiers().contains(Modifier.PRIVATE);
    }

    private static boolean isField(Element element) {
        return element.getKind() == ElementKind.FIELD;
    }

    private static boolean hasNoMotherIgnored(Element element) {
        return element.getAnnotation(Mother.Ignore.class) == null;
    }

    private boolean classContainSetter(Element field) {
        var setterName = "set" + capitalize(field.getSimpleName().toString());
        return typeElement.containSetter(setterName);
    }

    public List<Element> getFields() {
        return typeElement
                .getEnclosedElements()
                .stream()
                .map(Element.class::cast)
                .filter(GetFields::isField)
                .filter(GetFields::isPrivate)
                .filter(GetFields::hasNoMotherIgnored)
                .filter(e -> !onlyWithSetter || classContainSetter(e))
                .filter(e -> !onlySubClasses || (fieldIsComplexeClass(e) && !fieldIsEnum(e)))
                .filter(e -> !withoutSubClasses || (!fieldIsComplexeClass(e) || fieldIsEnum(e)))
                .toList();
    }
}
