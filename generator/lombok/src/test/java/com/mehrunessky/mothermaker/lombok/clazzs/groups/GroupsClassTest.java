package com.mehrunessky.mothermaker.lombok.clazzs.groups;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupsClassTest {

    @Test
    void testDefaultGroup() {
        // Create mother with default group and build instance
        var mother = GroupsClassMother.create();
        var instance = mother.build();

        // Verify that fields have the expected default values
        assertThat(instance.getStringWithGroups()).isEqualTo("defaultValue");
        assertThat(instance.getIntWithGroups()).isEqualTo(0); // No default value for default group
        assertThat(instance.getFieldWithoutDefault()).isEqualTo("fieldWithoutDefault");
    }

    @Test
    void testGroup1() {
        // Create mother with group1 and build instance
        var mother = GroupsClassMother.group1();
        var instance = mother.build();

        // Verify that fields have the expected group1 values
        assertThat(instance.getStringWithGroups()).isEqualTo("group1Value");
        assertThat(instance.getIntWithGroups()).isEqualTo(100);
        assertThat(instance.getFieldWithoutDefault()).isEqualTo("fieldWithoutDefault");
    }

    @Test
    void testGroup2() {
        // Create mother with group2 and build instance
        var mother = GroupsClassMother.group2();
        var instance = mother.build();

        // Verify that fields have the expected group2 values
        assertThat(instance.getStringWithGroups()).isEqualTo("group2Value");
        assertThat(instance.getIntWithGroups()).isEqualTo(200);
        assertThat(instance.getFieldWithoutDefault()).isEqualTo("fieldWithoutDefault");
    }

    @Test
    void testOverrideGroupValues() {
        // Create mother with group1, override values, and build instance
        var mother = GroupsClassMother.group1();
        var instance = mother
                .withStringWithGroups("overridden")
                .withIntWithGroups(999)
                .withFieldWithoutDefault("not null anymore")
                .build();

        // Verify that fields have the overridden values
        assertThat(instance.getStringWithGroups()).isEqualTo("overridden");
        assertThat(instance.getIntWithGroups()).isEqualTo(999);
        assertThat(instance.getFieldWithoutDefault()).isEqualTo("not null anymore");
    }
}
