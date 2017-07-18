package com.plynko.report;

import com.plynko.Page;

import java.io.IOException;

public interface Report {
    void execute(Page page) throws IOException;
}
