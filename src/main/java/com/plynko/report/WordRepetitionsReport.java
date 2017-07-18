package com.plynko.report;

import com.plynko.Page;

import java.io.IOException;
import java.util.Map;

public class WordRepetitionsReport implements Report {
    @Override
    public void execute(Page page) throws IOException {
        Map<String, Long> wordsSortedMap = page.getWordsSortedMap();
        wordsSortedMap.forEach((k, v) -> System.out.println(v + "\t" + k));
    }
}
