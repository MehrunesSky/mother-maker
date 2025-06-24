package com.mehrunessky.mothermaker.generators;

import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public interface Generator {

    TypeSpec generate(ProcessingEnvironment processingEnv, TypeElement typeElement);
}
