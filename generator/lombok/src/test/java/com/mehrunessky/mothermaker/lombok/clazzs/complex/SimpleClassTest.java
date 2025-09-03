package com.mehrunessky.mothermaker.lombok.clazzs.complex;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleClassTest {

    @Test
    void testDefaultValues() {
        // Create mother with default values and build instance
        var mother = SimpleClassMother.create();
        var instance = mother.build();

        // Verify that fields have default values
        assertThat(instance.getName()).isEqualTo("name");
        assertThat(instance.getValue()).isEqualTo(0);
    }

    @Test
    void testCustomValues() {
        // Create mother with custom values and build instance
        var mother = SimpleClassMother.create();
        var instance = mother
                .withName("Custom Name")
                .withValue(42)
                .build();

        // Verify that fields have the custom values
        assertThat(instance.getName()).isEqualTo("Custom Name");
        assertThat(instance.getValue()).isEqualTo(42);
    }

    @Test
    void testPartialCustomValues() {
        // Create mother with some custom values and build instance
        var mother = SimpleClassMother.create();
        var instance = mother
                .withName("Partial Custom")
                .build();

        // Verify that specified fields have custom values and others have defaults
        assertThat(instance.getName()).isEqualTo("Partial Custom");
        assertThat(instance.getValue()).isEqualTo(0); // Default value
    }
}