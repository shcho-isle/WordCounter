package com.plynko.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class ScannerLoader implements PageLoader {
    @Override
    public String loadPage(URL url) throws IOException {
        return new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
    }
}
