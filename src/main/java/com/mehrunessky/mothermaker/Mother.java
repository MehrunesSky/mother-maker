package com.mehrunessky.mothermaker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Mother {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    @Repeatable(Defaults.class)
    public @interface Default {
        String group() default "";

        String value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Defaults {

        Default[] value();
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Ignore {
    }
}