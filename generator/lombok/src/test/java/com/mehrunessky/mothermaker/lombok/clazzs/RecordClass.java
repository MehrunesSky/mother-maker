package com.mehrunessky.mothermaker.lombok.clazzs;

import com.mehrunessky.mothermaker.Mother;
import lombok.Builder;

@Mother
@Builder
public record RecordClass(String name, int age) {

}
