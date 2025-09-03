package com.mehrunessky.mothermaker.lombok.clazzs.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumFieldsClassTest {

    @Test
    void testEnumWithDefault() {
        // Create mother and build instance
        var mother = EnumFieldsClassMother.create();
        var instance = mother.build();

        // Verify that the enum field with default has the expected value
        assertThat(instance.getEnumWithDefault()).isEqualTo(TestEnum.VALUE2);

        // Verify that the enum field without default has the first enum value
        assertThat(instance.getEnumField()).isEqualTo(TestEnum.VALUE1);
    }

    @Test
    void testOverrideEnumDefault() {
        // Create mother, override default, and build instance
        var mother = EnumFieldsClassMother.create();
        var instance = mother
                .withEnumWithDefault(TestEnum.VALUE3)
                .withEnumField(TestEnum.VALUE1)
                .build();

        // Verify that the enum fields have the expected values
        assertThat(instance.getEnumWithDefault()).isEqualTo(TestEnum.VALUE3);
        assertThat(instance.getEnumField()).isEqualTo(TestEnum.VALUE1);
    }
}
