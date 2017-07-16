package com.plynko.report;

import java.util.List;
import java.util.Map;

import static com.plynko.StringUtil.getWordsList;
import static com.plynko.StringUtil.getWordsSortedMap;

public class WordRepetitionsReport implements Report {
    @Override
    public void execute(String page) {
        List<String> wordsList = getWordsList(page);
        Map<String, Long> wordsSortedMap = getWordsSortedMap(wordsList);
        wordsSortedMap.forEach((k, v) -> System.out.println(v + "\t" + k));
    }
}
