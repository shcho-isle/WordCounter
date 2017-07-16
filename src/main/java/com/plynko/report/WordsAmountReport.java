package com.plynko.report;

import java.util.List;

import static com.plynko.StringUtil.getWordsList;

public class WordsAmountReport implements Report {
    @Override
    public void execute(String page) {
        List<String> wordsList = getWordsList(page);
        System.out.println("Total number of words is: " + wordsList.size());
    }
}
