package com.plynko;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Stores util methods to work with URLs and Strings.
 */
public final class StringUtil {

    // The regular expression that defines the entire set of possible delimiters.
    private static final String DELIMITERS = "([\\s,-.;]+)|(<[^<]*?>)";

    // The regular expression that defines what kind of words we want to exclude.
    private static final String IGNORED_WORDS = "^$|.*?[\\d\\p{Punct}]+.*";

    private static final List<String> IGNORED_TAGS = Arrays.asList("style", "script", "image", "object");

    // Suppresses default constructor, ensuring non-instantiability.
    private StringUtil() {}

    /**
     * Looks for all words on the page, excluding tags and {@code IGNORED_WORDS}.
     *
     * @param  page the string which represent page content.
     * @return the list of all found words.
     */
    public static List<String> getWordsList(String page) {
        String clearPage = IGNORED_TAGS.size() > 0 ? removeTags(page) : page;

        String[] wordsArray = clearPage.split(DELIMITERS);

        return Arrays.stream(wordsArray)
                .filter(w -> !w.matches(IGNORED_WORDS))
                .collect(Collectors.toList());
    }

    /**
     * Replaces {@code IGNORED_TAGS} and their content with spaces.
     *
     * @param  page the string which represent page content.
     * @return the string constructed by replacing each matching subsequence by the space.
     */
    private static String removeTags(String page) {
        String tags = IGNORED_TAGS.stream().collect(Collectors.joining("|"));
        String regex = String.format("<(%s)[^<]*?>.*?</\\1>", tags);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return pattern.matcher(page).replaceAll(" ");
    }

    /**
     * Counts the number of repetitions of each word and sorts words.
     *
     * @param  wordsList the list of all words.
     * @return the sorted map where the key is the number of repetitions and the value is the word.
     */
    public static Map<String, Long> getWordsSortedMap(List<String> wordsList) {
        return wordsList.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        TreeMap::new,
                        Collectors.counting()));
    }
}