package com.plynko.report;

import com.plynko.Page;

import java.io.IOException;

public class WordRepetitionsReport implements Report {
    @Override
    public void execute(Page page) throws IOException {
        page.getWordsSortedMap().forEach((k, v) -> System.out.println(v + "\t" + k));
    }
}
