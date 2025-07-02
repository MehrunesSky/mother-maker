package com.mehrunessky.mothermaker.domain;

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

/**
 * Wrapper for Element objects representing fields.
 * Provides additional functionality for working with field elements,
 * particularly for handling Mother annotations.
 */
@RequiredArgsConstructor(staticName = "of")
public class FieldElementWrapper {
    @Delegate
    private final Element element;

    /**
     * Gets the TypeElementWrapper for this field's type.
     *
     * @return The TypeElementWrapper for the field's type
     */
    public TypeElementWrapper getTypeElementWrapper() {
        return TypeElementWrapper.of((TypeElement) ((DeclaredType) element.asType()).asElement());
    }

    /**
     * Gets the simple name of the field.
     *
     * @return The field name as a string
     */
    public String getFieldName() {
        return element.getSimpleName().toString();
    }

    /**
     * Finds a default annotation with the specified group.
     *
     * @param group The group to search for, or empty string for global default
     * @return Optional containing the found annotation, or empty if not found
     */
    private Optional<Mother.Default> findDefaultAnnotation(String group) {
        return Optional
                .ofNullable(element.getAnnotation(Mother.Default.class))
                .filter(a -> a.group().equals(group))
                .or(() -> Optional.ofNullable(element
                                        .getAnnotation(Mother.Defaults.class)
                                )
                                .stream()
                                .flatMap(a -> Stream.of(a.value()))
                                .filter(a -> a.group().equals(group))
                                .findFirst()
                );
    }

    /**
     * Gets the global default value for this field.
     *
     * @return The default value as a string, or null if not specified
     */
    public String getDefaultValue() {
        return findDefaultAnnotation("")
                .map(Mother.Default::value)
                .orElse(null);
    }

    /**
     * Gets all groups defined for this field.
     *
     * @return A set of group names
     */
    public Set<String> getGroups() {
        return getGroupMap().keySet();
    }

    /**
     * Gets a map of group names to their default values.
     *
     * @return A map from group names to default values
     */
    public Map<String, String> getGroupMap() {
        var defaults = new ArrayList<Mother.Default>();
        if (element.getAnnotation(Mother.Defaults.class) != null) {
            defaults.addAll(Arrays.asList(element.getAnnotation(Mother.Defaults.class).value()));
        }
        if (element.getAnnotation(Mother.Default.class) != null) {
            defaults.add(element.getAnnotation(Mother.Default.class));
        }
        return defaults
                .stream()
                .filter(a -> !a.group().isEmpty())
                .collect(Collectors.toMap(Mother.Default::group, Mother.Default::value));
    }

    /**
     * Gets the default value for the specified group.
     *
     * @param group The group name, or empty string for global default
     * @return The default value for the group, or null if not specified
     */
    public String getValueForGroup(String group) {
        if (group.isEmpty()) {
            return getDefaultValue();
        }
        return getGroupMap().get(group);
    }

    /**
     * Gets the default value for the specified group, or the global default if not found.
     *
     * @param group The group name
     * @return The group's default value, or the global default, or null if neither exists
     */
    public String getValueForGroupOrDefault(String group) {
        return Optional.ofNullable(
                getValueForGroup(group)
        ).orElse(getDefaultValue());
    }

    /**
     * Checks if this field has a declared type.
     *
     * @return true if the field has a declared type, false otherwise
     */
    public boolean isDeclaredType() {
        return this.asType().getKind() == TypeKind.DECLARED;
    }

    /**
     * Checks if this field is a String.
     *
     * @return true if the field is a String, false otherwise
     */
    public boolean isStringType() {
        return isDeclaredType() && "java.lang.String"
                .equals(this.getTypeElementWrapper().getTypeElement().toString());
    }

    /**
     * Checks if this field is a collection type (List, Set, or Map).
     *
     * @return true if the field is a collection, false otherwise
     */
    public boolean isCollectionType() {
        return isDeclaredType() &&
                Set.of("java.util.List", "java.util.Set", "java.util.Map")
                        .contains(this.getTypeElementWrapper().getTypeElement().toString());
    }

    public boolean isList() {
        return isDeclaredType() &&
                Set.of("java.util.List")
                        .contains(this.getTypeElementWrapper().getTypeElement().toString());
    }
}
