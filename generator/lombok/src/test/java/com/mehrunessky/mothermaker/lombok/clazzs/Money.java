package com.mehrunessky.mothermaker.lombok.clazzs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Money {

    private Number amount;

    private String currency;
}
