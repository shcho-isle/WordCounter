package com.plynko;

import com.plynko.loader.PageLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Page {

    private static final List<String> ACCEPTABLE_PROTOCOLS = Arrays.asList("http", "test");

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
     * @param loader the string which represent URL to download.
     * @return the content of page as a string, if it starts with one of the {@code ACCEPTABLE_PAGE_PREFIXES};
     * otherwise, {@code null}.
     * @throws IOException              if an I/O exception occurs.
     * @throws IllegalArgumentException if URL does not has acceptable protocol.
     */
    public String getContent(PageLoader loader) throws IOException {
        if (content != null) {
            return content;
        }

        content = loader.loadPage(url);

        content = content.startsWith("\uFEFF") ? content.substring(1) : content;

        for (String prefix : ACCEPTABLE_PAGE_PREFIXES) {
            if (content.regionMatches(true, 0, prefix, 0, prefix.length())) {
                return content;
            }
        }

        return null;
    }

    /**
     * Looks for all words on the page, excluding tags and {@code IGNORED_WORDS}.
     *
     * @return the list of all found words.
     */
    public List<String> getWordsList() {
        if (wordsList != null) {
            return wordsList;
        }

        if (IGNORED_TAGS.size() > 0) {
            removeTags();
        }

        String[] wordsArray = content.split(DELIMITERS);

        wordsList = Arrays.stream(wordsArray)
                .filter(w -> !w.matches(IGNORED_WORDS))
                .collect(Collectors.toList());

        return wordsList;
    }

    /**
     * Replaces {@code IGNORED_TAGS} and their content with spaces.
     *
     * @return the string constructed by replacing each matching subsequence by the space.
     */
    private void removeTags() {
        String tags = IGNORED_TAGS.stream().collect(Collectors.joining("|"));
        String regex = String.format("<(%s)[^<]*?>.*?</\\1>", tags);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        content = pattern.matcher(content).replaceAll(" ");
    }

    /**
     * Counts the number of repetitions of each word and sorts words.
     *
     * @return the sorted map where the key is the number of repetitions and the value is the word.
     */
    public Map<String, Long> getWordsSortedMap() {
        if (wordsSortedMap != null) {
            return wordsSortedMap;
        }

        wordsSortedMap = wordsList.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        TreeMap::new,
                        Collectors.counting()));

        return wordsSortedMap;
    }
}
