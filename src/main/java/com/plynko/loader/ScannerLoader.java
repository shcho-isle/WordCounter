package com.plynko.loader;

import java.io.IOException;
import java.util.Scanner;

public class ScannerLoader extends PageLoader {
    @Override
    protected void loadPage() throws IOException  {
        page = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
    }
}
