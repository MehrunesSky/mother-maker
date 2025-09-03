package com.mehrunessky.mothermaker.lombok.clazzs.enums;

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
public class EnumFieldsClass {

    private TestEnum enumField;
    
    @Mother.Default("VALUE2")
    private TestEnum enumWithDefault;
}