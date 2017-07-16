package com.plynko.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public abstract class PageLoader {

    private static final List<String> ACCEPTABLE_PROTOCOLS = Arrays.asList("http", "test");

    // Sign by which we define the HTML content.
    private static final List<String> ACCEPTABLE_PAGE_PREFIXES = Arrays.asList("<!doctype html", "<html");

    protected URL url;

    protected String page;

    protected abstract void loadPage() throws IOException;

    /**
     * Checks if the given string is proper URL.
     * Downloads page from the Internet for the given URL.
     * Removes UTF8 BOM if present.
     * Checks if downloaded page has HTML content.
     *
     * @param  urlString the string which represent URL to download.
     * @return the downloaded page as a string, if it starts with one of the {@code ACCEPTABLE_PAGE_PREFIXES};
     *         otherwise, {@code null}.
     * @throws IOException if an I/O exception occurs.
     * @throws IllegalArgumentException if URL does not has acceptable protocol.
     */
    public String getPage(String urlString) throws IOException {
        if (page != null) {
            return page;
        }

        url = new URL(urlString);
        checkUrl();

        loadPage();

        page = page.startsWith("\uFEFF") ? page.substring(1) : page;

        for (String prefix : ACCEPTABLE_PAGE_PREFIXES) {
            if (page.regionMatches(true, 0, prefix, 0, prefix.length())) {
                return page;
            }
        }

        return null;
    }

    //Checks if the URL has one of the ACCEPTABLE_PROTOCOLS
    private void checkUrl() {
        String urlProtocol = url.getProtocol();
        for (String protocol : ACCEPTABLE_PROTOCOLS) {
            if (urlProtocol.equals(protocol)) {
                return;
            }
        }
        throw new IllegalArgumentException("Acceptable protocols: " + ACCEPTABLE_PROTOCOLS);
    }
}
