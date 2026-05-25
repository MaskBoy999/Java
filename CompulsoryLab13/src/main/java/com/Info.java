package com;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;
import java.util.Locale;

public class Info {
    public static void execute(Locale locale) {
        System.out.println("Country: " + locale.getDisplayCountry() + " (" + locale.getDisplayCountry(locale) + ")");
        System.out.println("Language: " + locale.getDisplayLanguage() + " (" + locale.getDisplayLanguage(locale) + ")");

        try {
            Currency currency = Currency.getInstance(locale);
            System.out.println("Currency: " + currency.getCurrencyCode() + " (" + currency.getDisplayName() + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Currency: N/A");
        }

        DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
        String[] weekdays = dfs.getWeekdays();
        System.out.print("Week Days: ");
        boolean first = true;
        for (int i = 1; i < weekdays.length; i++) {
            if (!weekdays[i].isEmpty()) {
                if (!first) System.out.print(", ");
                System.out.print(weekdays[i]);
                first = false;
            }
        }
        System.out.println();

        String[] months = dfs.getMonths();
        System.out.print("Months: ");
        for (int i = 0; i < months.length - 1; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(months[i]);
        }
        System.out.println();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter defaultFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault());
        DateTimeFormatter localeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale);
        System.out.println("Today: " + now.format(defaultFormatter) + " (" + now.format(localeFormatter) + ")");
    }
}
