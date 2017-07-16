package com.plynko;

import com.plynko.loader.PageLoader;
import com.plynko.report.Report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

    private PageLoader loader;

    private List<Report> reports = new ArrayList<>();

    private String urlString;

    public void serUrlString(String urlString) {
        this.urlString = urlString;
    }

    public void setLoader(PageLoader loader) {
        this.loader = loader;
    }

    public void addReport(Report report) {
        this.reports.add(report);
    }

    public void execute() throws IOException {
        String page = loader.getPage(urlString);

        if (page == null) {
            System.err.println("This URL – " + urlString + " - does not contain HTML content");
            return;
        }

        for (Report r: reports) {
            r.execute(page);
        }
    }
}
