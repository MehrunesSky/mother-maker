package com.mehrunessky.mothermaker.lombok.clazzs.defaults;

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
public class DefaultValuesClass {

    @Mother.Default("hello")
    private String stringWithDefault;

    @Mother.Default("42")
    private int intWithDefault;

    private String stringWithoutDefault;
    private int intWithoutDefault;
}