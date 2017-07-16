package com.plynko;

import com.plynko.loader.ScannerLoader;
import com.plynko.report.WordRepetitionsReport;
import com.plynko.report.WordsAmountReport;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No URL specified.");
        }

        String urlString = args[0];

        ReportBuilder builder = new ReportBuilder();

        builder.serUrlString(urlString);
        builder.setLoader(new ScannerLoader());
        builder.addReport(new WordsAmountReport());
        builder.addReport(new WordRepetitionsReport());
        builder.execute();
    }
}