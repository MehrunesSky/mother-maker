package com.mehrunessky.mothermaker.lombok.methods;

import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.experimental.UtilityClass;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.mehrunessky.mothermaker.utils.StringUtils.capitalize;

/**
 * Utility class for generating "with" methods for Mother classes.
 * These methods allow for fluent modification of field values in the generated Mother classes.
 */
@UtilityClass
public class GenerateWithMethods {

    /**
     * Generates a list of "with" methods for a Mother class.
     * Two types of methods are generated:
     * <ul>
     *   <li>Simple "with" methods for regular fields that directly set the value on the builder</li>
     *   <li>Function-based "with" methods for complex fields that apply a function to the field's Mother object</li>
     * </ul>
     *
     * @param typeElement The type element wrapper for the class
     * @return A list of MethodSpec objects representing the generated methods
     */
    public List<MethodSpec> generateWithMethods(TypeElementWrapper typeElement) {
        List<MethodSpec> methods = new ArrayList<>();
        methods.addAll(generateMethodForSimpleFields(typeElement, false));
        methods.addAll(generateMethodForComplexFields(typeElement, false));
        return methods;
    }

    public List<MethodSpec> generateAbstractWithMethods(TypeElementWrapper typeElement) {
        List<MethodSpec> methods = new ArrayList<>();
        methods.addAll(generateMethodForSimpleFields(typeElement, true));
        methods.addAll(generateMethodForComplexFields(typeElement, true));
        return methods;
    }

    private static List<MethodSpec> generateMethodForSimpleFields(
            TypeElementWrapper typeElement, boolean abstractMethod) {
        List<MethodSpec> methods = new ArrayList<>();
        Modifier[] modifiers = abstractMethod ?
                new Modifier[]{Modifier.PUBLIC, Modifier.ABSTRACT} :
                new Modifier[]{Modifier.PUBLIC};

        // Generate simple "with" methods for regular fields
        for (FieldElementWrapper enclosedElement : typeElement.getPrimitiveFields()) {
            String fieldName = enclosedElement.getSimpleName().toString();
            TypeMirror fieldType = enclosedElement.asType();

            if (enclosedElement.isCollectionType()) {
                Class<?> clazz = null;
                if (enclosedElement.isList()) {
                    clazz = List.class;
                }
                if (clazz != null) {
                    MethodSpec.Builder builder = MethodSpec
                            .methodBuilder("withNo" + capitalize(fieldName))
                            .addModifiers(modifiers)
                            .returns(typeElement.containExtend() ? typeElement.getInterfaceMotherClassName() : typeElement.getMotherClassName());
                    if (!abstractMethod) {
                        builder
                                .addStatement("$N.$N($T.of())", "builder", fieldName, List.class)
                                .addStatement("return ($T) this", typeElement.containExtend() ? typeElement.getInterfaceMotherClassName() : typeElement.getMotherClassName());
                    }
                    methods.add(
                            builder
                                    .build()
                    );
                }
            }

            MethodSpec.Builder builder = MethodSpec
                    .methodBuilder("with" + capitalize(fieldName))
                    .addModifiers(modifiers)
                    .addParameter(TypeName.get(fieldType), fieldName)
                    .returns(typeElement.containExtend() ? typeElement.getInterfaceMotherClassName() : typeElement.getMotherClassName());
            if (!abstractMethod) {
                builder
                        .addStatement("$N.$N($L)", "builder", fieldName, fieldName)
                        .addStatement("return ($T) this", typeElement.containExtend() ? typeElement.getInterfaceMotherClassName() : typeElement.getMotherClassName());
            }
            methods.add(
                    builder
                            .build()
            );
        }
        return methods;
    }

    private static List<MethodSpec> generateMethodForComplexFields(
            TypeElementWrapper typeElement, boolean abstractMethod) {
        List<MethodSpec> methods = new ArrayList<>();
        Modifier[] modifiers = abstractMethod ?
                new Modifier[]{Modifier.PUBLIC, Modifier.ABSTRACT} :
                new Modifier[]{Modifier.PUBLIC};
        // Generate function-based "with" methods for complex fields
        for (var enclosedElement : typeElement.getComplexFields()) {
            var parameterFunctionName = enclosedElement.getMotherClassName();
            String fieldName = enclosedElement.getSimpleName().toString() + "Function";

            MethodSpec.Builder builder = MethodSpec
                    .methodBuilder("with" + capitalize(enclosedElement.getSimpleName().toString()))
                    .addModifiers(modifiers)
                    .addParameter(ParameterizedTypeName.get(ClassName.get(Function.class), parameterFunctionName, parameterFunctionName), fieldName)
                    .returns(typeElement.containExtend() ? typeElement.getInterfaceMotherClassName() : typeElement.getMotherClassName());
            if (!abstractMethod) {
                builder
                        .addStatement("$N = $N.apply($L)", enclosedElement.getFieldName(), fieldName, enclosedElement.getFieldName())
                        .addStatement("$N.$N($N.build())", "builder", enclosedElement.getSimpleName().toString(), enclosedElement.getFieldName())
                        .addStatement("return ($T) this", typeElement.containExtend() ? typeElement.getInterfaceMotherClassName() : typeElement.getMotherClassName());
            }
            methods.add(
                    builder
                            .build()
            );
        }
        return methods;
    }
}
