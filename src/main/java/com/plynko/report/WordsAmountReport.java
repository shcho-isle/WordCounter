package com.plynko.report;

import com.plynko.Page;

import java.io.IOException;
import java.util.List;

public class WordsAmountReport implements Report {
    @Override
    public void execute(Page page) throws IOException {
        List<String> wordsList = page.getWordsList();
        System.out.println("Total number of words is: " + wordsList.size());
    }
}
