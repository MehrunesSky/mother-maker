package com.mehrunessky.mothermaker.lombok.clazzs.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionFieldsClassTest {

    @Test
    void testCollectionFields() {
        // Create test data
        List<String> testList = Arrays.asList("one", "two", "three");
        Set<Integer> testSet = new HashSet<>(Arrays.asList(1, 2, 3));
        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("one", 1);
        testMap.put("two", 2);
        testMap.put("three", 3);

        // Create mother and set collection fields
        var mother = CollectionFieldsClassMother.create();
        var instance = mother
                .withStringList(testList)
                .withIntegerSet(testSet)
                .withStringIntegerMap(testMap)
                .build();

        // Verify that the collection fields have the expected values
        assertThat(instance.getStringList()).containsExactlyElementsOf(testList);
        assertThat(instance.getIntegerSet()).containsExactlyInAnyOrderElementsOf(testSet);
        assertThat(instance.getStringIntegerMap()).containsAllEntriesOf(testMap);
    }

    @Test
    void testEmptyCollections() {
        // Create mother without setting collection fields
        var mother = CollectionFieldsClassMother.create();
        var instance = mother.build();

        // Verify that the collection fields are empty by default
        assertThat(instance.getStringList()).isEmpty();
        assertThat(instance.getIntegerSet()).isEmpty();
        assertThat(instance.getStringIntegerMap()).isEmpty();
    }
}
