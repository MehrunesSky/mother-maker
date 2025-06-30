package com.mehrunessky.mothermaker.lombok;

import com.mehrunessky.mothermaker.domain.FieldElementWrapper;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

public class LombokConstructorGenerator {

    public static MethodSpec generate(TypeElementWrapper typeElementWrapper) {
        var constructor = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(typeElementWrapper.getLombokBuilderClassName(), "builder", Modifier.FINAL);

        for (var element : typeElementWrapper.getComplexFields()) {
            constructor.addParameter(
                    element.getTypeElementWrapper().getMotherClassName(),
                    element.getFieldName(),
                    Modifier.FINAL
            );
        }

        constructor.addStatement("this.builder = builder");

        for (FieldElementWrapper element : typeElementWrapper.getComplexFields()) {
            constructor.addStatement("this.$N = $N",
                    element.getFieldName(),
                    element.getFieldName()
            );
        }

        return constructor.build();
    }
}
