package com.plynko;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringUtils {

    public static final List<String> ACCEPTABLE_PROTOCOLS = Arrays.asList("http", "test");
    private static final List<String> ACCEPTABLE_PAGE_PREFIXES = Arrays.asList("<!doctype html", "<html");
    private static final String DELIMITERS = "([\\s,-.;]+)|(<[^<]*?>)";
    private static final String IGNORED_WORDS = "^$|.*?[\\d\\p{Punct}]+.*";
    private static final List<String> IGNORED_TAGS = Arrays.asList("style", "script", "image", "object");

    private StringUtils() {
    }

    public static void checkUrl(URL url) {
        for (String protocol : ACCEPTABLE_PROTOCOLS) {
            if (url.getProtocol().equals(protocol)) {
                return;
            }
        }
        throw new IllegalArgumentException("Acceptable protocols: " + ACCEPTABLE_PROTOCOLS);
    }

    public static String getPage(URL url) throws IOException {
        String page = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
        page = removeUTF8BOM(page.trim());
        for (String prefix : ACCEPTABLE_PAGE_PREFIXES) {
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
        if (IGNORED_TAGS.size() > 0) {
            page = removeTags(page);
        }

        String[] wordsArray = page.split(DELIMITERS);

        return Arrays.stream(wordsArray)
                .filter(w -> !w.matches(IGNORED_WORDS))
                .collect(Collectors.toList());
    }

    private static String removeTags(String page) {
        String tags = IGNORED_TAGS.stream().collect(Collectors.joining("|"));
        String regex = String.format("<(%s)[^<]*?>.*?</\\1>", tags);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return pattern.matcher(page).replaceAll(" ");
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