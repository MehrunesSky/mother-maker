package com.mehrunessky.mothermaker.clazzs;

import com.mehrunessky.mothermaker.Mother;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Mother
public class SubClass {

    @Mother.Default(group = "Two", value = "twoValue")
    private String field;

    @Mother.Default("defaultValue")
    @Mother.Default(group = "One", value = "crazy")
    private String field2;
}
