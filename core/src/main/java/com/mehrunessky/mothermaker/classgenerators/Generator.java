package com.mehrunessky.mothermaker.classgenerators;

import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public interface Generator {

    TypeSpec generate(ProcessingEnvironment processingEnv, TypeElement typeElement);

    TypeSpec generateInterface(ProcessingEnvironment processingEnv, TypeElement typeElement);
}
