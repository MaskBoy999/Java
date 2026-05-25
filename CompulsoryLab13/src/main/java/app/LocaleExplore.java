package app;

import com.DisplayLocales;
import com.Info;
import com.SetLocale;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ResourceBundle messages = ResourceBundle.getBundle("res.Messages", SetLocale.getCurrentLocale());

        while (true) {
            System.out.print(messages.getString("prompt") + " ");
            String command = scanner.nextLine().trim();

            if (command.equals("display locales")) {
                System.out.println(messages.getString("locales"));
                DisplayLocales.execute();
            } else if (command.startsWith("set locale ")) {
                String languageTag = command.substring("set locale ".length());
                SetLocale.execute(languageTag);
                messages = ResourceBundle.getBundle("res.Messages", SetLocale.getCurrentLocale());
                String pattern = messages.getString("locale.set");
                System.out.println(MessageFormat.format(pattern, SetLocale.getCurrentLocale().getDisplayName()));
            } else if (command.startsWith("info")) {
                String tag = command.length() > 5 ? command.substring(5).trim() : "";
                Locale locale;
                if (tag.isEmpty()) {
                    locale = SetLocale.getCurrentLocale();
                } else {
                    locale = Locale.forLanguageTag(tag);
                }
                String pattern = messages.getString("info");
                System.out.println(MessageFormat.format(pattern, locale.getDisplayName()));
                Info.execute(locale);
            } else if (command.equals("exit") || command.equals("quit")) {
                break;
            } else {
                System.out.println(messages.getString("invalid"));
            }
        }
    }
}
