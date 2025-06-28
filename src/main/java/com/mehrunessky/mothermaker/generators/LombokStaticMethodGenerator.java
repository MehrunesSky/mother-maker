package com.mehrunessky.mothermaker.generators;

import com.mehrunessky.mothermaker.datagenerator.DataProvider;
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
import java.util.stream.Collectors;

@UtilityClass
public class LombokStaticMethodGenerator {

    public static List<MethodSpec> generate(TypeElementWrapper typeElementWrapper) {
        var groups = typeElementWrapper
                .getFields()
                .stream()
                .flatMap(f -> f.getGroups().stream())
                .filter(g -> !g.isEmpty())
                .collect(Collectors.toSet());

        List<MethodSpec> methods = new ArrayList<>();
        methods.add(generate("", typeElementWrapper));
        groups
                .stream()
                .map(g -> generate(g, typeElementWrapper))
                .forEach(methods::add);
        return methods;
    }

    private static MethodSpec generate(String group, TypeElementWrapper typeElementWrapper) {
        var codeBlockBuilder = CodeBlock
                .builder();

        for (var element : typeElementWrapper.getComplexFields()) {
            var c = ClassName.get((TypeElement) ((DeclaredType) element.asType()).asElement());
            codeBlockBuilder.addStatement(
                    "$T $N = $T.create()",
                    ClassName.get(c.packageName(), c.simpleName() + "Mother"),
                    element.getSimpleName().toString(),
                    ClassName.get(c.packageName(), c.simpleName() + "Mother")
            );
        }

        codeBlockBuilder
                .add("return new $T($T\n    .builder()\n", typeElementWrapper.getMotherClassName(), typeElementWrapper.getClassName());
        for (Element enclosedElement : GetFields.of(typeElementWrapper.getTypeElement()).getFields()) {
            var data = DataProvider.getData(
                    group, FieldElementWrapper.of(enclosedElement)
            );
            data.ifPresent(tuple -> {
                var l = new ArrayList<>();
                l.add(enclosedElement.getSimpleName().toString());
                l.addAll(Arrays.asList(tuple.object()));

                codeBlockBuilder
                        .add(tuple.statement(),
                                l.toArray(Object[]::new)
                        );
            });
        }

        var jsp = typeElementWrapper
                .getComplexFields()
                .stream()
                .map(element -> {
                    var c = ClassName.get((TypeElement) ((DeclaredType) element.asType()).asElement());
                    return "    " + element.getFieldName();
                })
                .collect(Collectors.joining(",\n"));

        if (!jsp.isEmpty()) {
            codeBlockBuilder.add("," + jsp);
        }

        var codeBlock = codeBlockBuilder.addStatement(")")
                .build();

        return MethodSpec
                .methodBuilder(group.isEmpty() ? "create" : StringUtils.removeCapitalize(group))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(typeElementWrapper.getMotherClassName())
                .addCode(codeBlock)
                .build();
    }
}
