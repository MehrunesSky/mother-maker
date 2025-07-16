package com.mehrunessky.mothermaker.clazzs.custom;

import com.mehrunessky.mothermaker.clazzs.Money;

public class MoneyCustomMother {
    private final Money.MoneyBuilder builder;

    private MoneyCustomMother(final Money.MoneyBuilder builder) {
        this.builder = builder;
    }

    public static MoneyCustomMother create() {
        return new MoneyCustomMother(Money
                .builder()
                .amount(3)
                .currency("EUR")
        );
    }

    public MoneyCustomMother withAmount(Number amount) {
        builder.amount(amount);
        return this;
    }

    public MoneyCustomMother withCurrency(String currency) {
        builder.currency(currency);
        return this;
    }

    public Money build() {
        return builder.build();
    }
}
