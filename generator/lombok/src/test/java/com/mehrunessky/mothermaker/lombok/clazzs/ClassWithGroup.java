package com.mehrunessky.mothermaker.lombok.clazzs;

import com.mehrunessky.mothermaker.Mother;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Mother
public class ClassWithGroup {

    @Mother.Default("defaultValue")
    @Mother.Default(group = "One", value = "defaultValueForGroupOne")
    private String fieldFromClassWithGroup;

    @Mother.Default("12")
    @Mother.Default(group = "One", value = "50")
    private int value;

    @Mother.Default("create")
    @Mother.Default(group = "Two", value = "one")
    private SubClass subClass;

    @Mother.Default("one")
    @Mother.Default(group = "Two", value = "create")
    private SubClass otherSubClass;
}
