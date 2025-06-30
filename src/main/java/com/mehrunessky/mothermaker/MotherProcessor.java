package com.mehrunessky.mothermaker;

import com.mehrunessky.mothermaker.classgenerators.ClassicGenerator;
import com.mehrunessky.mothermaker.classgenerators.Generator;
import com.mehrunessky.mothermaker.lombok.LombokBuilderGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import lombok.Builder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("com.mehrunessky.mothermaker.Mother")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class MotherProcessor extends AbstractProcessor {

    private static final Generator CLASSIC_GENERATOR = new ClassicGenerator();
    private static final Generator LOMBOK_GENERATOR = new LombokBuilderGenerator();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Mother.class)) {
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.RECORD) {
                TypeElement typeElement = (TypeElement) element;
                if (typeElement.getAnnotation(Builder.class) != null) {
                    generateMotherClass(processingEnv, LOMBOK_GENERATOR, typeElement);
                } else {
                    generateMotherClass(processingEnv, CLASSIC_GENERATOR, typeElement);
                }
            }
        }
        return true;
    }

    private void generateMotherClass(ProcessingEnvironment processingEnvironment, Generator generator, TypeElement typeElement) {
        try {
            JavaFile.builder(ClassName.get(typeElement).packageName(), generator.generate(processingEnvironment, typeElement))
                    .build()
                    .writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Erreur génération " + e.getMessage());
        }
    }
}