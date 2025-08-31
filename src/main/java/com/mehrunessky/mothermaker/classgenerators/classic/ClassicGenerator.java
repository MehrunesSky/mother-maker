package com.mehrunessky.mothermaker.classgenerators.classic;

import com.mehrunessky.mothermaker.classgenerators.Generator;
import com.mehrunessky.mothermaker.utils.GetFields;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.mehrunessky.mothermaker.utils.StringUtils.capitalize;
import static com.mehrunessky.mothermaker.utils.StringUtils.removeCapitalize;

public class ClassicGenerator implements Generator {
    @Override
    public TypeSpec generate(ProcessingEnvironment processingEnv, TypeElement typeElement) {
        // ici tu utilises JavaPoet pour créer la classe <Nom>Mother

        ClassName className = ClassName.get(typeElement);

        String packageName = className.packageName();
        String motherClassName = className.simpleName() + "Mother";

        var classBuilder = TypeSpec.classBuilder(motherClassName)
                .addModifiers(Modifier.PUBLIC)
                .addField(
                        FieldSpec
                                .builder(className, removeCapitalize(className.simpleName()), Modifier.PRIVATE, Modifier.FINAL)
                                .initializer("new $T()", className)
                                .build()
                );
        for (Element enclosedElement : GetFields.of(typeElement).withOnlyWithSetter(true).getFields()) {
            var fieldName = enclosedElement.getSimpleName().toString();
            var fieldType = enclosedElement.asType();
            var setterName = "set" + capitalize(fieldName);
            classBuilder.addMethod(
                    MethodSpec
                            .methodBuilder("with" + capitalize(fieldName))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(TypeName.get(fieldType), fieldName)
                            .returns(ClassName.get(packageName, motherClassName))
                            .addStatement("$N.$N($L)", removeCapitalize(className.simpleName()), setterName, fieldName)
                            .addStatement("return this")
                            .build()
            );
        }

        classBuilder.addMethod(
                MethodSpec
                        .methodBuilder("build")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ClassName.get(packageName, className.simpleName()))
                        .addStatement("return $L", removeCapitalize(className.simpleName()))
                        .build()
        );

        return classBuilder.build();
    }

    @Override
    public TypeSpec generateInterface(ProcessingEnvironment processingEnv, TypeElement typeElement) {
        return null;
    }
}
