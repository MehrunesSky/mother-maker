package com.mehrunessky.mothermaker;

import com.mehrunessky.mothermaker.clazzs.ClassWithGroupMother;
import com.mehrunessky.mothermaker.clazzs.EnumClass;
import com.mehrunessky.mothermaker.clazzs.OtherClass;
import com.mehrunessky.mothermaker.clazzs.OtherClassMother;
import com.mehrunessky.mothermaker.clazzs.RecordClass;
import com.mehrunessky.mothermaker.clazzs.RecordClassMother;
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

        @Nested
        class clazz {
            @Test
            void basic() {
                var otherClass = OtherClassMother
                        .create()
                        .build();

                assertThat(otherClass)
                        .isEqualTo(new OtherClass(
                                        "hello",
                                        10,
                                        List.of(),
                                        Set.of(),
                                        Map.of(),
                                        new SubClass("field", "defaultValue"),
                                        new SubClass("field", "defaultValue"),
                                        EnumClass.VALUE1,
                                        EnumClass.VALUE2
                                )
                        );
            }

            @Test
            void change() {
                var otherClass = OtherClassMother
                        .create()
                        .withAString("otherCoucou")
                        .withNumber(2)
                        .withEnumClass(EnumClass.VALUE2)
                        .build();

                assertThat(otherClass)
                        .isEqualTo(new OtherClass(
                                        "otherCoucou",
                                        2,
                                        List.of(),
                                        Set.of(),
                                        Map.of(),
                                        new SubClass("field", "defaultValue"),
                                        new SubClass("field", "defaultValue"),
                                        EnumClass.VALUE2,
                                        EnumClass.VALUE2
                                )
                        );
            }

            @Test
            void changeSubClass() {
                var otherClass = OtherClassMother
                        .create()
                        .withAString("otherCoucou")
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
                                        new SubClass("field", "defaultValue"),
                                        EnumClass.VALUE1,
                                        EnumClass.VALUE2
                                )
                        );
            }

            @Nested
            class Collection {

                @Test
                void setListCollection() {
                    var otherClass = OtherClassMother
                            .create()
                            .withList(List.of("1", "2", "3"))
                            .build();

                    assertThat(otherClass.getList()).containsExactly("1", "2", "3");
                }

                @Test
                void setNoListCollection() {
                    var otherClass = OtherClassMother
                            .create()
                            .withList(List.of("1", "2", "3"))
                            .withNoList()
                            .build();

                    assertThat(otherClass.getList()).isEmpty();
                }

                @Test
                void setSetCollection() {
                    var otherClass = OtherClassMother
                            .create()
                            .withSet(Set.of("1", "2", "3"))
                            .build();

                    assertThat(otherClass.getSet()).containsOnly("1", "2", "3");
                }

                @Test
                void setMapCollection() {
                    var otherClass = OtherClassMother
                            .create()
                            .withMap(Map.of("k1", "v1", "k2", "v2"))
                            .build();

                    assertThat(otherClass.getMap())
                            .containsExactlyEntriesOf(Map.of("k1", "v1", "k2", "v2"));
                }
            }

            @Nested
            class Group {

                @Test
                void groupDefault() {
                    var group = ClassWithGroupMother
                            .create()
                            .build();

                    assertThat(group.getFieldFromClassWithGroup()).isEqualTo("defaultValue");
                    assertThat(group.getValue()).isEqualTo(12);
                }

                @Test
                void groupDefaultWhenNoSpecific() {
                    var group = ClassWithGroupMother
                            .two()
                            .build();

                    assertThat(group.getFieldFromClassWithGroup()).isEqualTo("defaultValue");
                    assertThat(group.getValue()).isEqualTo(12);
                }

                @Test
                void groupCustom() {
                    var group = ClassWithGroupMother
                            .one()
                            .build();

                    assertThat(group.getFieldFromClassWithGroup()).isEqualTo("defaultValueForGroupOne");
                    assertThat(group.getValue()).isEqualTo(50);
                }
            }
        }

        @Nested
        class recordd {

            @Test
            void basic() {
                var recordClass = RecordClassMother.create().build();
                assertThat(recordClass).isEqualTo(new RecordClass("name", 0));
            }

            @Test
            void change() {
                var recordClass = RecordClassMother
                        .create()
                        .withName("MyNAME")
                        .withAge(3)
                        .build();
                assertThat(recordClass).isEqualTo(new RecordClass("MyNAME", 3));
            }
        }
    }
}