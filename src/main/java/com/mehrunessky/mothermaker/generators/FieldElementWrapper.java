package com.mehrunessky.mothermaker.generators;

import com.mehrunessky.mothermaker.Mother;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return Optional
                .ofNullable(element.getAnnotation(Mother.Default.class))
                .filter(a -> a.group().isEmpty())
                .or(() -> Optional.ofNullable(element
                                        .getAnnotation(Mother.Defaults.class)
                                )
                                .stream()
                                .flatMap(a -> Stream.of(a.value()))
                                .filter(a -> a.group().isEmpty())
                                .findFirst()
                )
                .isPresent();
    }

    public String getDefaultValue() {
        return Optional
                .ofNullable(element.getAnnotation(Mother.Default.class))
                .filter(a -> a.group().isEmpty())
                .or(() -> Optional.ofNullable(element
                                        .getAnnotation(Mother.Defaults.class)
                                )
                                .stream()
                                .flatMap(d -> Stream.of(d.value()))
                                .filter(d -> d.group().isEmpty())
                                .findFirst()
                )
                .map(Mother.Default::value)
                .orElse(null);
    }

    public Set<String> getGroups() {
        return getGroupMap().keySet();
    }

    public Map<String, String> getGroupMap() {
        var l = new ArrayList<Mother.Default>();
        if (element.getAnnotation(Mother.Defaults.class) != null) {
            l.addAll(Arrays.asList(element.getAnnotation(Mother.Defaults.class).value()));
        }
        if (element.getAnnotation(Mother.Default.class) != null) {
            l.add(element.getAnnotation(Mother.Default.class));
        }
        return l
                .stream()
                .filter(a -> !a.group().isEmpty())
                .collect(Collectors.toMap(Mother.Default::group, Mother.Default::value));
    }

    public String getValueForGroup(String group) {
        if (group.isEmpty()) {
            return getDefaultValue();
        }
        return getGroupMap().get(group);
    }

    public boolean isDeclaredType() {
        return this.asType().getKind() == TypeKind.DECLARED;
    }

    public boolean isStringType() {
        return isDeclaredType() && "java.lang.String"
                .equals(this.getTypeElementWrapper().getTypeElement().toString());
    }

    public boolean isCollectionType() {
        return isDeclaredType() &&
                Set.of("java.util.List", "java.util.Set", "java.util.Map")
                        .contains(this.getTypeElementWrapper().getTypeElement().toString());
    }
}
