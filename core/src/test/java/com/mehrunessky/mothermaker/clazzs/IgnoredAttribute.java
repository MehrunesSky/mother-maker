package com.mehrunessky.mothermaker.clazzs;

import com.mehrunessky.mothermaker.Mother;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Mother
public class IgnoredAttribute {

    private String field;

    @Mother.Ignore
    private String ignored;
}
