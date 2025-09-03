package com.mehrunessky.mothermaker.lombok.clazzs.groups;

import com.mehrunessky.mothermaker.Mother;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Mother
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupsClass {

    @Mother.Default("defaultValue")
    @Mother.Default(group = "group1", value = "group1Value")
    @Mother.Default(group = "group2", value = "group2Value")
    private String stringWithGroups;

    @Mother.Default(group = "group1", value = "100")
    @Mother.Default(group = "group2", value = "200")
    private int intWithGroups;

    private String fieldWithoutDefault;
}