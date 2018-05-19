package com.plynko.report;

import com.plynko.Page;

import java.io.IOException;

public class WordsAmountReport implements Report {
    @Override
    public void execute(Page page) throws IOException {
        System.out.println("Total number of words is: " + page.getWordsNumber());
    }
}
