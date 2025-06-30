package com.mehrunessky.mothermaker.lombok;

import com.mehrunessky.mothermaker.classgenerators.Generator;
import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.mehrunessky.mothermaker.utils.GetFields;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

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
                    element.getSimpleName().toString(),
                    Modifier.PRIVATE
            );
        }

        classBuilder.addMethod(LombokConstructorGenerator.generate(typeElementWrapper));

        classBuilder.addMethods(LombokStaticMethodGenerator.generate(typeElementWrapper));

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

        for (var enclosedElement : typeElementWrapper.getComplexFields()) {
            var parameterFunctionName = enclosedElement.getTypeElementWrapper().getMotherClassName();
            String fieldName = enclosedElement.getSimpleName().toString() + "Function";

            classBuilder.addMethod(
                    MethodSpec
                            .methodBuilder("with" + capitalize(enclosedElement.getSimpleName().toString()))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), parameterFunctionName, parameterFunctionName), fieldName)
                            .returns(typeElementWrapper.getMotherClassName())
                            .addStatement("$N = $N.apply($L)", enclosedElement.getFieldName(), fieldName, enclosedElement.getFieldName())
                            .addStatement("$N.$N($N.build())", "builder", enclosedElement.getSimpleName().toString(), enclosedElement.getFieldName())
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
