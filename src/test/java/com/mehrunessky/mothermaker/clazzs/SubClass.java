package com.mehrunessky.mothermaker.clazzs;

import com.mehrunessky.mothermaker.Mother;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Mother
public class SubClass {

    private String field;

    private String field2;
}
