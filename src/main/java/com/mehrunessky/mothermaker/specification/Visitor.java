package com.mehrunessky.mothermaker.specification;

public interface Visitor<T, R> {
    R visit(DataGenerationSpecification.FromPredicate<T> tPredicate);

    R visit(DataGenerationSpecification.And<T> tAnd);
}
