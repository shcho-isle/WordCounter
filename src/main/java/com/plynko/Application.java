package com.plynko;

import com.plynko.builder.ReportBuilder;
import com.plynko.report.WordRepetitionsReport;
import com.plynko.report.WordsAmountReport;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No URL specified.");
        }

        Page page = new Page(args[0]);
        ReportBuilder builder = new ReportBuilder(page);

        builder.addReport(new WordsAmountReport());
        builder.addReport(new WordRepetitionsReport());

        builder.execute();
    }
}