package com.mehrunessky.mothermaker.datagenerator;

import com.mehrunessky.mothermaker.generators.FieldElementWrapper;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class DataProvider {

    public static Optional<Tuple> getData(FieldElementWrapper fieldElementWrapper) {
        if (DataGenerator.INSTANCE.test(fieldElementWrapper)) {
            return DataGenerator.INSTANCE.getData(fieldElementWrapper);
        }
        return Optional.empty();
    }
}
