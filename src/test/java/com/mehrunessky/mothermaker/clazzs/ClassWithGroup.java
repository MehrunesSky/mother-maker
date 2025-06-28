package com.mehrunessky.mothermaker.clazzs;

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
}
