package com;

import java.util.Locale;

public class SetLocale {
    private static Locale currentLocale = Locale.getDefault();

    public static void execute(String languageTag) {
        currentLocale = Locale.forLanguageTag(languageTag);
        Locale.setDefault(currentLocale);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}
