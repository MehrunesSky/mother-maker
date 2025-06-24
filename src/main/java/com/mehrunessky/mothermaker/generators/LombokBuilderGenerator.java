package com.mehrunessky.mothermaker.generators;

import com.mehrunessky.mothermaker.datagenerator.DataGenerator;
import com.mehrunessky.mothermaker.utils.ElementUtils;
import com.mehrunessky.mothermaker.utils.GetFields;
import com.mehrunessky.mothermaker.utils.StringUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mehrunessky.mothermaker.utils.StringUtils.capitalize;

public class LombokBuilderGenerator implements Generator {

    @Override
    public TypeSpec generate(ProcessingEnvironment processingEnv, TypeElement typeElement) {
        TypeElementWrapper typeElementWrapper = TypeElementWrapper.of(typeElement);

        var classBuilder = TypeSpec.classBuilder(typeElementWrapper.getMotherClassName())
                .addAnnotation(AnnotationSpec
                        .builder(ClassName.get(Generated.class))
                        .addMember("value", "$S", LombokBuilderGenerator.class.getName())
                        .build()
                )
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        typeElementWrapper.getLombokBuilderClassName(),
                        "builder",
                        Modifier.FINAL,
                        Modifier.PRIVATE
                );
        for (FieldElementWrapper element : typeElementWrapper.getComplexFields()) {
            classBuilder.addField(
                    element.getTypeElementWrapper().getMotherClassName(),
                    StringUtils.removeCapitalize(element.getTypeElementWrapper().getMotherClassName().simpleName()),
                    Modifier.PRIVATE
            );
        }

        var constructor = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(typeElementWrapper.getLombokBuilderClassName(), "builder", Modifier.FINAL);

        for (Element element : GetFields.of(typeElement).withOnlySubClasses(true).getFields()) {
            var c = ClassName.get((TypeElement) ((DeclaredType) element.asType()).asElement());
            var clazz = ClassName.get(c.packageName(), c.simpleName() + "Mother");
            constructor.addParameter(clazz, StringUtils.removeCapitalize(clazz.simpleName()), Modifier.FINAL);
        }

        constructor.addStatement("this.builder = builder");

        for (Element element : GetFields.of(typeElement).withOnlySubClasses(true).getFields()) {
            var c = ClassName.get((TypeElement) ((DeclaredType) element.asType()).asElement());
            var clazz = ClassName.get(c.packageName(), c.simpleName() + "Mother");
            constructor.addStatement("this.$N = $N", StringUtils.removeCapitalize(clazz.simpleName()), StringUtils.removeCapitalize(clazz.simpleName()));
        }

        classBuilder.addMethod(constructor
                .build()
        );

        var codeBlockBuilder = CodeBlock
                .builder();

        for (Element element : GetFields.of(typeElement).withOnlySubClasses(true).getFields()) {
            var c = ClassName.get((TypeElement) ((DeclaredType) element.asType()).asElement());
            var clazz = ClassName.get(c.packageName(), c.simpleName() + "Mother");
            codeBlockBuilder.addStatement(
                    "$T $N = $T.create()",
                    ClassName.get(c.packageName(), c.simpleName() + "Mother"),
                    StringUtils.removeCapitalize(clazz.simpleName()),
                    ClassName.get(c.packageName(), c.simpleName() + "Mother")
            );
        }

        codeBlockBuilder
                .add("return new $T($T\n    .builder()\n", typeElementWrapper.getMotherClassName(), typeElementWrapper.getClassName());
        for (Element enclosedElement : GetFields.of(typeElement).getFields()) {
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

        var jsp = GetFields
                .of(typeElement)
                .withOnlySubClasses(true)
                .getFields()
                .stream()
                .map(element -> {
                    var c = ClassName.get((TypeElement) ((DeclaredType) element.asType()).asElement());
                    return "    " + StringUtils.removeCapitalize(c.simpleName()) + "Mother";
                })
                .collect(Collectors.joining(","));

        if (!jsp.isEmpty()) {
            codeBlockBuilder.add("," + jsp);
        }

        var codeBlock = codeBlockBuilder.addStatement(")")
                .build();

        var methodSpec = MethodSpec
                .methodBuilder("create")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(typeElementWrapper.getMotherClassName())
                .addCode(codeBlock);
        classBuilder.addMethod(
                methodSpec.build()
        );

        for (Element enclosedElement : GetFields.of(typeElement).withWithoutSubClasses(true).getFields()) {
            String fieldName = enclosedElement.getSimpleName().toString();
            TypeMirror fieldType = enclosedElement.asType();
            classBuilder.addMethod(
                    MethodSpec
                            .methodBuilder("with" + capitalize(fieldName))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(TypeName.get(fieldType), fieldName)
                            .returns(typeElementWrapper.getMotherClassName())
                            .addStatement("$N.$N($L)", "builder", fieldName, fieldName)
                            .addStatement("return this")
                            .build()
            );
        }

        for (Element enclosedElement : GetFields.of(typeElement).withOnlySubClasses(true).getFields()) {
            String fieldName = enclosedElement.getSimpleName().toString() + "Function";
            var c = ClassName.get((TypeElement) ((DeclaredType) enclosedElement.asType()).asElement());
            var parameter = ClassName.get(c.packageName(), c.simpleName() + "Mother");

            classBuilder.addMethod(
                    MethodSpec
                            .methodBuilder("with" + capitalize(enclosedElement.getSimpleName().toString()))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), parameter, parameter), fieldName)
                            .returns(typeElementWrapper.getMotherClassName())
                            .addStatement("$N = $N.apply($L)", StringUtils.removeCapitalize(parameter.simpleName()), fieldName, StringUtils.removeCapitalize(parameter.simpleName()))
                            .addStatement("$N.$N($N.build())", "builder", enclosedElement.getSimpleName().toString(), StringUtils.removeCapitalize(parameter.simpleName()))
                            .addStatement("return this")
                            .build()
            );
        }

        classBuilder.addMethod(
                MethodSpec
                        .methodBuilder("build")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(typeElementWrapper.getClassName())
                        .addStatement("return builder.build()")
                        .build()
        );
        return classBuilder.build();
    }
}
