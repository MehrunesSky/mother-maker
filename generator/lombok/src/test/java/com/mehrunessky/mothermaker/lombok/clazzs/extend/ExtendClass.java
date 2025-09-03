package com.mehrunessky.mothermaker.lombok.clazzs.extend;

import com.mehrunessky.mothermaker.Mother;
import lombok.Builder;
import lombok.Data;

@Mother(extend = MyExtendClass.class)
@Data
@Builder
public class ExtendClass {

    private String aString;
}
