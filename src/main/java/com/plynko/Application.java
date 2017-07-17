package com.plynko;

import com.plynko.builder.ReportBuilder;
import com.plynko.director.ReportDirector;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No URL specified.");
        }

        ReportDirector director = new ReportDirector();

        Page page = new Page(args[0]);
        ReportBuilder builder = new ReportBuilder(page);
        director.constructAmountAndRepetitionsReport(builder);

        builder.execute();
    }
}