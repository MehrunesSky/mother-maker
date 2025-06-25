package com.mehrunessky.mothermaker.generators;

import com.mehrunessky.mothermaker.datagenerator.DataGenerator;
import com.mehrunessky.mothermaker.utils.ElementUtils;
import com.mehrunessky.mothermaker.utils.GetFields;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.stream.Collectors;

@UtilityClass
public class LombokStaticMethodGenerator {

    public static MethodSpec generate(TypeElementWrapper typeElementWrapper) {
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
            if (ElementUtils.fieldIsComplexeClass(enclosedElement)) {
                var c = ClassName.get((TypeElement) ((DeclaredType) enclosedElement.asType()).asElement());
                codeBlockBuilder
                        .add("    .$N($T.create().build())\n",
                                enclosedElement.getSimpleName().toString(),
                                ClassName.get(c.packageName(), c.simpleName() + "Mother")
                        );
                continue;
            }
            codeBlockBuilder
                    .add("    .$N($L)\n",
                            enclosedElement.getSimpleName().toString(),
                            DataGenerator.getData(
                                    enclosedElement.asType(),
                                    enclosedElement.getSimpleName().toString()
                            ).orElse(null)
                    );
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
                .methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(typeElementWrapper.getMotherClassName())
                .addCode(codeBlock)
                .build();
    }
}
