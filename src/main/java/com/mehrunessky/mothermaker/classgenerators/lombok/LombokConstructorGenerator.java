package com.mehrunessky.mothermaker.classgenerators.lombok;

import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

/**
 * Utility class for generating constructors for Mother classes.
 * This class creates a private constructor that takes a Lombok builder instance
 * and any complex fields as parameters.
 */
public class LombokConstructorGenerator {

    /**
     * Generates a constructor for a Mother class.
     * The constructor takes a builder instance and all complex fields as parameters,
     * and initializes the corresponding fields in the Mother class.
     *
     * @param typeElementWrapper The type element wrapper for the class
     * @return A MethodSpec representing the generated constructor
     */
    public static MethodSpec generate(TypeElementWrapper typeElementWrapper) {
        var constructor = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(typeElementWrapper.getLombokBuilderClassName(), "builder", Modifier.FINAL);

        // Add parameters for complex fields
        for (var element : typeElementWrapper.getComplexFields()) {
            constructor.addParameter(
                    element.containCustomMother() ?
                            element.getCustomMotherClassName().get() :
                            element.getTypeElementWrapper().getMotherClassName(),
                    element.getFieldName(),
                    Modifier.FINAL
            );
        }

        // Initialize the builder field
        constructor.addStatement("this.builder = builder");

        // Initialize complex fields
        for (FieldElementWrapper element : typeElementWrapper.getComplexFields()) {
            constructor.addStatement("this.$N = $N",
                    element.getFieldName(),
                    element.getFieldName()
            );
        }

        return constructor.build();
    }
}
