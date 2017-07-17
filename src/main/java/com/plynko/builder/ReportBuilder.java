package com.plynko.builder;

import com.plynko.Page;
import com.plynko.loader.PageLoader;
import com.plynko.report.Report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

    private Page page;

    private PageLoader loader;

    private List<Report> reports = new ArrayList<>();

    public ReportBuilder(Page page) {
        this.page = page;
    }

    public void setLoader(PageLoader loader) {
        this.loader = loader;
    }

    public void addReport(Report report) {
        this.reports.add(report);
    }

    public void execute() throws IOException {
        String content = page.getContent(loader);

        if (content == null) {
            System.err.println(String.format("This URL – %s - does not contain HTML content", page.getUrlString()));
            return;
        }

        for (Report r : reports) {
            r.execute(page);
        }
    }
}
