package com.mehrunessky.mothermaker;

import com.mehrunessky.mothermaker.classgenerators.Generator;
import com.mehrunessky.mothermaker.domain.TypeElementWrapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;

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
import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.Set;

@SupportedAnnotationTypes("com.mehrunessky.mothermaker.Mother")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class MotherProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Mother.class)) {
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.RECORD) {
                TypeElement typeElement = (TypeElement) element;
                var generators = ServiceLoader.load(
                        Generator.class, Generator.class.getClassLoader()
                );

                // Find the first generator that accepts the type element
                var generator = generators
                        .stream()
                        .map(ServiceLoader.Provider::get)
                        .sorted(Comparator.comparing(Generator::priority))
                        .filter(g -> g.accepts(typeElement))
                        .findFirst();

                if (generator.isPresent()) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Using generator: " + generator.get().getClass().getName());
                    generateMotherClass(processingEnv, generator.get(), typeElement);
                } else {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                            "No suitable generator found via ServiceLoader for " + typeElement.getQualifiedName() +
                                    ". Ensure that the Generator implementation is properly registered in META-INF/services.");
                }
            }
        }
        return true;
    }

    private void generateMotherClass(ProcessingEnvironment processingEnvironment, Generator generator, TypeElement typeElement) {
        try {
            if (TypeElementWrapper.of(typeElement).containExtend()) {
                JavaFile.builder(ClassName.get(typeElement).packageName(), generator.generateInterface(processingEnvironment, typeElement))
                        .build()
                        .writeTo(processingEnv.getFiler());
            }
            JavaFile.builder(ClassName.get(typeElement).packageName(), generator.generate(processingEnvironment, typeElement))
                    .build()
                    .writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Erreur génération " + e.getMessage());
        }
    }
}
