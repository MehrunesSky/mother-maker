package com.mehrunessky.mothermaker.clazzs.extend;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtendClassTest {

    @Test
    void testExtend() {
        var mother = ExtendClassMother.create();
        assertThat(mother.customString().build().getAString())
                .isEqualTo("customString");
    }
}
