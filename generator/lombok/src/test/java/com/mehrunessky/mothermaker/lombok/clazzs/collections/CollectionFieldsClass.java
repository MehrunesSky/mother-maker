package com.mehrunessky.mothermaker.lombok.clazzs.collections;

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
public class CollectionFieldsClass {

    private List<String> stringList;
    private Set<Integer> integerSet;
    private Map<String, Integer> stringIntegerMap;
}