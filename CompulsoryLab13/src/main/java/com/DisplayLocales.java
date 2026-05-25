package com;

import java.util.Locale;

public class DisplayLocales {
    public static void execute() {
        Locale[] available = Locale.getAvailableLocales();
        for (Locale locale : available) {
            System.out.println(locale.getDisplayName());
        }
    }
}
