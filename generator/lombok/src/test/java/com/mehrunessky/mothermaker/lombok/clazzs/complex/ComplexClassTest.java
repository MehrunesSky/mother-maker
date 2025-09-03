package com.mehrunessky.mothermaker.lombok.clazzs.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplexClassTest {

    @Test
    void testComplexObjectFields() {
        // Create a SimpleClass mother and build an instance
        var simpleMother = SimpleClassMother.create();
        var simpleInstance = simpleMother
                .withName("Test Name")
                .withValue(42)
                .build();

        // Create another SimpleClass instance
        var anotherSimpleMother = SimpleClassMother.create();
        var anotherSimpleInstance = anotherSimpleMother
                .withName("Another Name")
                .withValue(100)
                .build();

        // Create a ComplexClass mother and set the complex object fields
        var complexMother = ComplexClassMother.create();
        var complexInstance = complexMother
                .withId("test-id")
                .withSimpleObject(mother -> mother
                        .withName("Test Name")
                        .withValue(42))
                .withAnotherSimpleObject(mother -> mother
                        .withName("Another Name")
                        .withValue(100))
                .build();

        // Verify that the complex object fields have the expected values
        assertThat(complexInstance.getId()).isEqualTo("test-id");

        assertThat(complexInstance.getSimpleObject()).isNotNull();
        assertThat(complexInstance.getSimpleObject().getName()).isEqualTo("Test Name");
        assertThat(complexInstance.getSimpleObject().getValue()).isEqualTo(42);

        assertThat(complexInstance.getAnotherSimpleObject()).isNotNull();
        assertThat(complexInstance.getAnotherSimpleObject().getName()).isEqualTo("Another Name");
        assertThat(complexInstance.getAnotherSimpleObject().getValue()).isEqualTo(100);
    }

    @Test
    void testNullComplexObjectFields() {
        // Create a ComplexClass mother without setting the complex object fields
        var complexMother = ComplexClassMother.create();
        var complexInstance = complexMother
                .withId("test-id")
                .build();

        // Verify that the complex object fields have default values
        assertThat(complexInstance.getId()).isEqualTo("test-id");
        assertThat(complexInstance.getSimpleObject()).isNotNull();
        assertThat(complexInstance.getAnotherSimpleObject()).isNotNull();
    }
}
