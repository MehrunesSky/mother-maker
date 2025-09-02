package com.mehrunessky.mothermaker.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String removeCapitalize(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    public static String addDoubleQuotes(String string) {
        return "\"" + string + "\"";
    }
}
