package com.plynko;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringUtils {

    private static final List<String> ACCEPTABLE_URL_PREFIXES = Arrays.asList("http://");
    private static final List<String> ACCEPTABLE_PAGE_PREFIXES = Arrays.asList("<!DOCTYPE HTML", "<html");
    private static final String DELIMITERS = "([\\s,-.;]+)|(<[^<]+?>)";
    private static final String IGNORED_WORDS = "^$|.*?[\\d\\p{Punct}]+.*";
    private static final List<String> IGNORED_TAGS = Arrays.asList("style", "script", "image", "object");

    private StringUtils() {
    }

    public static void checkUrl(String url) {
        for (String prefix : ACCEPTABLE_URL_PREFIXES) {
            if (startsWith(url, prefix)) {
                return;
            }
        }
        throw new IllegalArgumentException("Acceptable URL prefixes:" + ACCEPTABLE_URL_PREFIXES);
    }

    public static String getPage(String url) throws IOException {
        String page = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        page = removeUTF8BOM(page.trim());
        for (String prefix : ACCEPTABLE_PAGE_PREFIXES) {
            if (startsWith(page, prefix)) {
                return page;
            }
        }
        throw new IllegalArgumentException("This URL – " + url + " - does not contain HTML content");
    }

    public static boolean startsWith(String s, String prefix) {
        return s.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static String removeUTF8BOM(String s) {
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        return s;
    }

    public static List<String> getWordsList(String page) {
        for (String tag : IGNORED_TAGS) {
            String regex = "<" + tag + ".*?>.*?</" + tag + ">";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            page = pattern.matcher(page).replaceAll(" ");
        }
        String[] wordsArray = page.split(DELIMITERS);
        return Arrays.stream(wordsArray)
                .filter(w -> !w.matches(IGNORED_WORDS))
                .collect(Collectors.toList());
    }

    public static Map<String, Long> getWordsSortedMap(List<String> wordsList) {
        Map<String, Long> countedWords = wordsList.stream()
                .collect(
                        Collectors.groupingBy(Function.identity(), Collectors.counting())
                );
        return new TreeMap<>(countedWords);
    }
}
