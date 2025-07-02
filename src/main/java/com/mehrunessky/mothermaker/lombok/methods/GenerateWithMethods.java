package com.mehrunessky.mothermaker.lombok.methods;

import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.mehrunessky.mothermaker.utils.GetFields;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.mehrunessky.mothermaker.utils.StringUtils.capitalize;

@UtilityClass
public class GenerateWithMethods {

    public List<MethodSpec> generateWithMethods(TypeElementWrapper typeElement) {
        List<MethodSpec> methods = new ArrayList<>();
        for (Element enclosedElement : GetFields.of(typeElement).withWithoutSubClasses(true).getFields()) {
            String fieldName = enclosedElement.getSimpleName().toString();
            TypeMirror fieldType = enclosedElement.asType();
            methods.add(
                    MethodSpec
                            .methodBuilder("with" + capitalize(fieldName))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(TypeName.get(fieldType), fieldName)
                            .returns(typeElement.getMotherClassName())
                            .addStatement("$N.$N($L)", "builder", fieldName, fieldName)
                            .addStatement("return this")
                            .build()
            );
        }

        for (var enclosedElement : typeElement.getComplexFields()) {
            var parameterFunctionName = enclosedElement.getTypeElementWrapper().getMotherClassName();
            String fieldName = enclosedElement.getSimpleName().toString() + "Function";

            methods.add(
                    MethodSpec
                            .methodBuilder("with" + capitalize(enclosedElement.getSimpleName().toString()))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), parameterFunctionName, parameterFunctionName), fieldName)
                            .returns(typeElement.getMotherClassName())
                            .addStatement("$N = $N.apply($L)", enclosedElement.getFieldName(), fieldName, enclosedElement.getFieldName())
                            .addStatement("$N.$N($N.build())", "builder", enclosedElement.getSimpleName().toString(), enclosedElement.getFieldName())
                            .addStatement("return this")
                            .build()
            );
        }
        return methods;
    }
}
