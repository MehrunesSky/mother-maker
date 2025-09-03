package com.mehrunessky.mothermaker.lombok.clazzs.records;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleRecordTest {

    @Test
    void testDefaultValues() {
        // Create mother with default values and build instance
        var mother = SimpleRecordMother.create();
        var record = mother.build();

        // Verify that fields have the expected default values
        assertThat(record.name()).isEqualTo("Default Name");
        assertThat(record.age()).isEqualTo(25);
        assertThat(record.active()).isTrue();
        assertThat(record.description()).isEqualTo("description");
    }

    @Test
    void testCustomValues() {
        // Create mother with custom values and build instance
        var mother = SimpleRecordMother.create();
        var record = mother
                .withName("Custom Name")
                .withAge(30)
                .withActive(false)
                .withDescription("Custom Description")
                .build();

        // Verify that fields have the custom values
        assertThat(record.name()).isEqualTo("Custom Name");
        assertThat(record.age()).isEqualTo(30);
        assertThat(record.active()).isFalse();
        assertThat(record.description()).isEqualTo("Custom Description");
    }

    @Test
    void testPartialCustomValues() {
        // Create mother with some custom values and build instance
        var mother = SimpleRecordMother.create();
        var record = mother
                .withName("Partial Custom")
                .withDescription("Some Description")
                .build();

        // Verify that specified fields have custom values and others have defaults
        assertThat(record.name()).isEqualTo("Partial Custom");
        assertThat(record.age()).isEqualTo(25); // Default value
        assertThat(record.active()).isTrue(); // Default value
        assertThat(record.description()).isEqualTo("Some Description");
    }
}
