package com.plynko.director;

import com.plynko.builder.ReportBuilder;
import com.plynko.loader.ScannerLoader;
import com.plynko.report.WordRepetitionsReport;
import com.plynko.report.WordsAmountReport;

public class ReportDirector {

    public void constructAmountAndRepetitionsReport(ReportBuilder builder) {
        builder.setLoader(new ScannerLoader());
        builder.addReport(new WordsAmountReport());
        builder.addReport(new WordRepetitionsReport());
    }
}
