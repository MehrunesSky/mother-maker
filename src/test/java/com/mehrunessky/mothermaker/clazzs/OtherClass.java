package com.mehrunessky.mothermaker.clazzs;

import com.mehrunessky.mothermaker.Mother;
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
    private int number;
    private List<String> list;
    private Set<String> set;
    private Map<String, String> map;
    private SubClass aClass;
    private SubClass twoClass;
    private EnumClass enumClass;
}
