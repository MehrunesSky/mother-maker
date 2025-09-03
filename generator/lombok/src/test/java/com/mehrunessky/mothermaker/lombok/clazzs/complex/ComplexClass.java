package com.mehrunessky.mothermaker.lombok.clazzs.complex;

import com.mehrunessky.mothermaker.Mother;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Mother
public class ComplexClass {
    private String id;
    private SimpleClass simpleObject;
    private SimpleClass anotherSimpleObject;
}