package com.plynko;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.plynko.StringUtils.*;

public class Counter {
    public static void main(String[] args) throws IOException {
        URL url = new URL(args[0]);
        checkUrl(url);
        String page = getPage(url);

        List<String> wordsList = getWordsList(page);
        System.out.println("Total number of words is: " + wordsList.size());

        Map<String, Long> wordsSortedMap = getWordsSortedMap(wordsList);
        wordsSortedMap.forEach((k, v) -> System.out.println(v + "\t" + k));
    }
}
