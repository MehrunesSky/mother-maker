package com.mehrunessky.mothermaker.lombok.clazzs.defaults;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultValuesClassTest {

    @Test
    void testDefaultValues() {
        var mother = DefaultValuesClassMother.create();
        var instance = mother.build();

        // Test that fields with default values have the expected values
        assertThat(instance.getStringWithDefault()).isEqualTo("hello");
        assertThat(instance.getIntWithDefault()).isEqualTo(42);

        // Test that fields without default values have the generated default values
        assertThat(instance.getStringWithoutDefault()).isEqualTo("stringWithoutDefault");
        assertThat(instance.getIntWithoutDefault()).isEqualTo(0);
    }

    @Test
    void testOverrideDefaultValues() {
        var mother = DefaultValuesClassMother.create();
        var instance = mother
                .withStringWithDefault("overridden")
                .withIntWithDefault(100)
                .build();

        // Test that default values can be overridden
        assertThat(instance.getStringWithDefault()).isEqualTo("overridden");
        assertThat(instance.getIntWithDefault()).isEqualTo(100);
    }
}
