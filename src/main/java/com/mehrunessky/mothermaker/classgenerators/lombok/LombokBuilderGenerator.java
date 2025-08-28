package com.mehrunessky.mothermaker.classgenerators.lombok;

import com.mehrunessky.mothermaker.classgenerators.Generator;
import com.mehrunessky.mothermaker.classgenerators.lombok.methods.GenerateWithMethods;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Generator implementation for Lombok-based classes.
 * This generator creates a "Mother" class that uses the Lombok builder pattern
 * to facilitate test data creation for classes annotated with {@code @Mother}.
 */
public class LombokBuilderGenerator implements Generator {

    /**
     * Generates a TypeSpec for a "Mother" class based on the provided TypeElement.
     * The generated class includes:
     * <ul>
     *   <li>A private final builder field</li>
     *   <li>Fields for complex types</li>
     *   <li>A private constructor</li>
     *   <li>Static factory methods for different groups</li>
     *   <li>"with" methods for field manipulation</li>
     *   <li>A build method that returns the built object</li>
     * </ul>
     *
     * @param processingEnv The processing environment
     * @param typeElement   The type element to generate a Mother class for
     * @return A TypeSpec representing the generated Mother class
     */
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
                    element.getMotherClassName(),
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
