package com.mehrunessky.mothermaker.lombok;

import com.mehrunessky.mothermaker.classgenerators.Generator;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.mehrunessky.mothermaker.lombok.methods.GenerateWithMethods;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

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
                    element.getSimpleName().toString(),
                    Modifier.PRIVATE
            );
        }

        classBuilder.addMethod(LombokConstructorGenerator.generate(typeElementWrapper));

        classBuilder.addMethods(LombokStaticMethodGenerator.generate(typeElementWrapper));

        classBuilder.addMethods(
                GenerateWithMethods.generateWithMethods(
                        typeElementWrapper
                )
        );

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
