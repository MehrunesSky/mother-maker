package com.mehrunessky.mothermaker.specification;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public interface DataGenerationSpecification<T> {

    record FromPredicate<T>(Predicate<T> p) implements DataGenerationSpecification<T> {

        @Override
        public boolean test(T t) {
            return p.test(t);
        }

        @Override
        public <R> R accept(Visitor<T, R> visitor) {
            return visitor.visit(this);
        }
    }

    record And<T>(List<DataGenerationSpecification<T>> l) implements DataGenerationSpecification<T> {

        @Override
        public boolean test(T t) {
            return l.stream().allMatch(spec -> spec.test(t));
        }

        @Override
        public <R> R accept(Visitor<T, R> visitor) {
            return visitor.visit(this);
        }
    }

    static <T> DataGenerationSpecification<T> of(Predicate<T> p) {
        return new FromPredicate<>(p);
    }

    static <T> DataGenerationSpecification<T> and(DataGenerationSpecification<T>... p) {
        return new And<>(Arrays.asList(p));
    }

    default DataGenerationSpecification<T> and(DataGenerationSpecification<T> p) {
        return new And<>(List.of(this, p));
    }

    boolean test(T t);

    <V> V accept(Visitor<T, V> visitor);
}
