package com.plynko;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringUtils {

    private final static ResourceBundle config = ResourceBundle.getBundle("com.plynko.config");

    private StringUtils() {
    }

    public static void checkUrl(URL url) {
        String[] protocols = config.getString("protocols").split(",");

        for (String protocol : protocols) {
            if (url.getProtocol().equals(protocol)) {
                return;
            }
        }

        throw new IllegalArgumentException("Acceptable protocols: " + Arrays.toString(protocols));
    }

    public static String getPage(URL url) throws IOException {
        String page = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
        page = removeUTF8BOM(page.trim());

        String[] prefixes = config.getString("prefixes").split(",");
        for (String prefix : prefixes) {
            if (startsWith(page, prefix)) {
                return page;
            }
        }

        throw new IllegalArgumentException("This URL – " + url + " - does not contain HTML content");
    }

    private static boolean startsWith(String s, String prefix) {
        return s.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    private static String removeUTF8BOM(String s) {
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        return s;
    }

    public static List<String> getWordsList(String page) {
        String[] ignoredTags = config.getString("ignored_tags").split(",");

        if (ignoredTags.length > 0) {
            StringBuilder regexBuilder = new StringBuilder("");
            String or = "";
            for (String tag : ignoredTags) {
                regexBuilder.append(or).append(tag);
                or = "|";
            }

            String regex = String.format("<(%s)[^<]*?>.*?</\\1>", regexBuilder.toString());
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            page = pattern.matcher(page).replaceAll(" ");
        }

        String[] wordsArray = page.split(config.getString("delimiters"));

        return Arrays.stream(wordsArray)
                .filter(w -> !w.matches(config.getString("ignored_words")))
                .collect(Collectors.toList());
    }

    public static Map<String, Long> getWordsSortedMap(List<String> wordsList) {
        return wordsList.stream()
                .collect(
                        Collectors.groupingBy(
                                Function.identity(),
                                TreeMap::new,
                                Collectors.counting())
                );
    }
}