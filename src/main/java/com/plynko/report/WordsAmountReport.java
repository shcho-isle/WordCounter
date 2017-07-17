package com.plynko.report;

import com.plynko.Page;

import java.util.List;

public class WordsAmountReport implements Report {
    @Override
    public void execute(Page page) {
        List<String> wordsList = page.getWordsList();
        System.out.println("Total number of words is: " + wordsList.size());
    }
}
