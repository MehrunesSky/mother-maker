package com.mehrunessky.mothermaker.specification;

import com.mehrunessky.mothermaker.generators.TypeElementWrapper;
import com.mehrunessky.mothermaker.specification.DataGenerationSpecification.And;
import com.mehrunessky.mothermaker.specification.DataGenerationSpecification.FromPredicate;

public class GetDefaultDataVisitorImpl implements Visitor<DataGenerationSpecification<TypeElementWrapper>, String> {
    @Override
    public String visit(FromPredicate<DataGenerationSpecification<TypeElementWrapper>> tPredicate) {
        return "";
    }

    @Override
    public String visit(And<DataGenerationSpecification<TypeElementWrapper>> dataGenerationSpecificationAnd) {
        return "";
    }
}
