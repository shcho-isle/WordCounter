package com.plynko.loader;

import java.io.IOException;
import java.net.URL;

public interface PageLoader {
    String loadPage(URL url) throws IOException;
}
