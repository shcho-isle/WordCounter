package com.plynko.builder;

import com.plynko.Page;
import com.plynko.report.Report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

    private Page page;

    private List<Report> reports = new ArrayList<>();

    public ReportBuilder(Page page) {
        this.page = page;
    }

    public void addReport(Report report) {
        this.reports.add(report);
    }

    public void execute() throws IOException {
        for (Report r : reports) {
            r.execute(page);
        }
    }
}
