package com.mehrunessky.mothermaker.lombok.clazzs;

import com.mehrunessky.mothermaker.Mother;
import com.mehrunessky.mothermaker.lombok.clazzs.custom.MoneyCustomMother;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Mother
public class OtherClass {

    @Mother.Default("hello")
    private String aString;

    @Mother.Default("10")
    private int number;

    private List<String> list;
    private Set<String> set;
    private Map<String, String> map;
    private SubClass aClass;
    private SubClass twoClass;
    @Mother.Use(MoneyCustomMother.class)
    private Money money;
    private EnumClass enumClass;
    @Mother.Default("VALUE2")
    private EnumClass enum2;
}
