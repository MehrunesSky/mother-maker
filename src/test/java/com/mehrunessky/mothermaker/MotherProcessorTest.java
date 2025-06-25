package com.mehrunessky.mothermaker;

import com.mehrunessky.mothermaker.clazzs.OtherClass;
import com.mehrunessky.mothermaker.clazzs.OtherClassMother;
import com.mehrunessky.mothermaker.clazzs.SubClass;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MotherProcessorTest {

    @Nested
    class Lombok {

        @Test
        void basic() {
            var otherClass = OtherClassMother
                    .create()
                    .build();

            assertThat(otherClass)
                    .isEqualTo(new OtherClass(
                                    "coucou",
                                    0,
                                    List.of(),
                                    Set.of(),
                                    Map.of(),
                                    new SubClass("field", "field2"),
                                    new SubClass("field", "field2")
                            )
                    );
        }

        @Test
        void change() {
            var otherClass = OtherClassMother
                    .create()
                    .withCoucou("otherCoucou")
                    .withNumber(2)
                    .build();

            assertThat(otherClass)
                    .isEqualTo(new OtherClass(
                                    "otherCoucou",
                                    2,
                                    List.of(),
                                    Set.of(),
                                    Map.of(),
                                    new SubClass("field", "field2"),
                                    new SubClass("field", "field2")
                            )
                    );
        }

        @Test
        void changeSubClass() {
            var otherClass = OtherClassMother
                    .create()
                    .withCoucou("otherCoucou")
                    .withNumber(2)
                    .withAClass(s -> s.withField("car"))
                    .withAClass(s -> s.withField2("otherValue"))
                    .build();

            assertThat(otherClass)
                    .isEqualTo(new OtherClass(
                                    "otherCoucou",
                                    2,
                                    List.of(),
                                    Set.of(),
                                    Map.of(),
                                    new SubClass("car", "otherValue"),
                                    new SubClass("field", "field2")
                            )
                    );
        }
    }
}