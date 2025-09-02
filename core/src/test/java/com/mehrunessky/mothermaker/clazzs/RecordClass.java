package com.mehrunessky.mothermaker.clazzs;

import com.mehrunessky.mothermaker.Mother;
import lombok.Builder;

@Mother
@Builder
public record RecordClass(String name, int age) {

}
