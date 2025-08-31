package com.mehrunessky.mothermaker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Main annotation to mark a class for Mother class generation.
 * When applied to a class, a corresponding "Mother" class will be generated
 * to facilitate test data creation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Mother {

    Class<?> extend() default Void.class;

    /**
     * Annotation to specify default values for fields.
     * Can be used multiple times on the same field with different group values.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    @Repeatable(Defaults.class)
    @interface Default {
        /**
         * The group this default value belongs to.
         * When empty, it's considered the global default.
         * When specified, the value will only be used when creating objects with that group.
         *
         * @return The group name
         */
        String group() default "";

        /**
         * The default value for the field.
         *
         * @return The default value as a string
         */
        String value();
    }

    /**
     * Container annotation for multiple {@link Default} annotations.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Defaults {
        /**
         * The array of Default annotations.
         *
         * @return Array of Default annotations
         */
        Default[] value();
    }

    /**
     * Annotation to mark a field to be ignored during Mother class generation.
     * Fields marked with this annotation won't have corresponding "with" methods
     * and won't be initialized with default values.
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    @interface Ignore {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    @interface Use {
        Class<?> value();

        String method() default "create";
    }
}
