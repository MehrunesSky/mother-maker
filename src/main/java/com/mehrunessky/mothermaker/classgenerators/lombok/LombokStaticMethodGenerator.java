package com.mehrunessky.mothermaker.classgenerators.lombok;

import com.mehrunessky.mothermaker.datagenerators.DataProvider;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.mehrunessky.mothermaker.utils.GetFields;
import com.mehrunessky.mothermaker.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for generating static factory methods for Mother classes.
 * This class creates methods for the default case and for each group defined in the fields.
 */
@UtilityClass
public class LombokStaticMethodGenerator {

    /**
     * Generates a list of static factory methods for a Mother class.
     * Creates one method for the default case and one for each group defined in the fields.
     *
     * @param typeElementWrapper The type element wrapper for the class
     * @return A list of MethodSpec objects representing the generated methods
     */
    public static List<MethodSpec> generate(TypeElementWrapper typeElementWrapper) {
        // Extract all unique, non-empty group names from fields
        var groups = typeElementWrapper
                .getFields()
                .stream()
                .flatMap(f -> f.getGroups().stream())
                .filter(g -> !g.isEmpty())
                .collect(Collectors.toSet());

        // Generate methods for default and each group
        List<MethodSpec> methods = new ArrayList<>();
        methods.add(generate("", typeElementWrapper)); // Default method (no group)

        // Generate methods for each group
        groups
                .stream()
                .map(g -> generate(g, typeElementWrapper))
                .forEach(methods::add);

        return methods;
    }

    /**
     * Generates a static factory method for a specific group.
     * The method creates a new instance of the Mother class with default values
     * appropriate for the specified group.
     *
     * @param group              The group name, or empty string for the default group
     * @param typeElementWrapper The type element wrapper for the class
     * @return A MethodSpec representing the generated method
     */
    private static MethodSpec generate(String group, TypeElementWrapper typeElementWrapper) {
        var codeBlockBuilder = CodeBlock.builder();

        // Add statements to create Mother objects for complex fields
        typeElementWrapper
                .getComplexFields()
                .forEach(element -> {
                    var c = ClassName.get((TypeElement) ((DeclaredType) element.asType()).asElement());
                    var field = Optional
                            .ofNullable(element.getValueForGroup(group))
                            .map(StringUtils::capitalize)
                            .map(s -> element.getFieldName() + s)
                            .orElse(element.getFieldName() + "Create");
                    ClassName motherClassName = element.containCustomMother() ?
                            (ClassName) element.getCustomMotherClassName().get() :
                            element.getTypeElementWrapper().getMotherClassName();
                    codeBlockBuilder.addStatement(
                            "$T $N = $T.$N()",
                            motherClassName,
                            field,
                            motherClassName,
                            Optional.ofNullable(element.getValueForGroup(group)).orElse("create")
                    );
                });

        // Start building the return statement with the builder
        codeBlockBuilder
                .add("return new $T($T\n    .builder()\n", typeElementWrapper.getMotherClassName(), typeElementWrapper.getClassName());

        // Add builder method calls for each field
        for (Element enclosedElement : GetFields.of(typeElementWrapper.getTypeElement()).getFields()) {
            try {
                var data = DataProvider.getData(
                        group, FieldElementWrapper.of(enclosedElement)
                );
                data.ifPresent(tuple -> {
                    var params = new ArrayList<>();
                    params.add(enclosedElement.getSimpleName().toString());
                    params.addAll(Arrays.asList(tuple.object()));

                    codeBlockBuilder
                            .add("    .$N(%s)\n".formatted(tuple.statement()),
                                    params.toArray(Object[]::new)
                            );
                });
            } catch (Exception e) {
                // Skip this field if there's an error processing it
            }
        }

        // Add complex field parameters to the constructor call
        var complexFieldParams = typeElementWrapper
                .getComplexFields()
                .stream()
                .map(element -> "    " + Optional
                        .ofNullable(element.getValueForGroup(group))
                        .map(StringUtils::capitalize)
                        .map(s -> element.getFieldName() + s)
                        .orElse(element.getFieldName() + "Create"))
                .collect(Collectors.joining(",\n"));

        if (!complexFieldParams.isEmpty()) {
            codeBlockBuilder.add("," + complexFieldParams);
        }

        // Complete the code block
        var codeBlock = codeBlockBuilder.addStatement(")")
                .build();

        // Build and return the method
        return MethodSpec
                .methodBuilder(group.isEmpty() ? "create" : StringUtils.removeCapitalize(group))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(typeElementWrapper.getMotherClassName())
                .addCode(codeBlock)
                .build();
    }
}
