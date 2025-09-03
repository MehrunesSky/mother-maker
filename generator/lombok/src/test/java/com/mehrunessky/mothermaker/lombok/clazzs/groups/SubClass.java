package com.mehrunessky.mothermaker.lombok.clazzs.groups;

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
    @Mother.Default(group = "Two", value = "otherCrazyValue")
    private String field2;
}