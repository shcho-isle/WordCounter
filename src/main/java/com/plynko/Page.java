package com.plynko;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Page {

    public static final List<String> ACCEPTABLE_PROTOCOLS = Arrays.asList("http", "test");

    // Sign by which we define the HTML content.
    private static final List<String> ACCEPTABLE_PAGE_PREFIXES = Arrays.asList("<!doctype html", "<html");

    // The regular expression that defines the entire set of possible delimiters.
    private static final String DELIMITERS = "([\\s,-.;]+)|(<[^<]*?>)";

    // The regular expression that defines what kind of words we want to exclude.
    private static final String IGNORED_WORDS = "^$|.*?[\\d\\p{Punct}]+.*";

    private static final List<String> IGNORED_TAGS = Arrays.asList("style", "script", "image", "object");

    private URL url;

    private String content;

    private List<String> wordsList;

    private Map<String, Long> wordsSortedMap;

    public String getUrlString() {
        return url.toString();
    }

    /**
     * Checks if the given string is proper URL.
     * Checks if the URL has one of the {@code ACCEPTABLE_PROTOCOLS}.
     *
     * @param urlString the string which represent URL to download.
     * @throws IllegalArgumentException if URL does not has acceptable protocol.
     */
    public Page(String urlString) throws MalformedURLException {
        url = new URL(urlString);

        String urlProtocol = url.getProtocol();
        for (String protocol : ACCEPTABLE_PROTOCOLS) {
            if (urlProtocol.equals(protocol)) {
                return;
            }
        }
        throw new IllegalArgumentException("Acceptable protocols: " + ACCEPTABLE_PROTOCOLS);
    }

    /**
     * Downloads page from the Internet for the given URL.
     * Removes UTF8 BOM if present.
     * Checks if downloaded page has HTML content.
     *
     * @return the content of page as a string, if it starts with one of the {@code ACCEPTABLE_PAGE_PREFIXES};
     * otherwise, {@code null}.
     * @throws IOException if an I/O exception occurs.
     */
    public String getContent() throws IOException {
        if (content != null) {
            return content;
        }

        content = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();

        content = content.startsWith("\uFEFF") ? content.substring(1) : content;

        for (String prefix : ACCEPTABLE_PAGE_PREFIXES) {
            if (content.regionMatches(true, 0, prefix, 0, prefix.length())) {
                return content;
            }
        }

        return null;
    }

    public List<String> getWordsList() throws IOException {
        if (wordsList != null) {
            return wordsList;
        }

        if (IGNORED_TAGS.size() > 0) {
            removeTags();
        }

        String[] wordsArray = getContent().split(DELIMITERS);

        wordsList = Arrays.stream(wordsArray)
                .filter(w -> !w.matches(IGNORED_WORDS))
                .collect(Collectors.toList());

        return wordsList;
    }

    private void removeTags() throws IOException {
        String tags = IGNORED_TAGS.stream().collect(Collectors.joining("|"));
        String regex = String.format("<(%s)[^<]*?>.*?</\\1>", tags);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        content = pattern.matcher(getContent()).replaceAll(" ");
    }

    public Map<String, Long> getWordsSortedMap() throws IOException {
        if (wordsSortedMap != null) {
            return wordsSortedMap;
        }

        wordsSortedMap = getWordsList().stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        TreeMap::new,
                        Collectors.counting()));

        return wordsSortedMap;
    }
}
