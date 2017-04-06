package com.plynko;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.plynko.StringUtils.*;

public class StringUtilTest {
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void checkUrlTest() throws MalformedURLException {
        checkUrl(new URL("http://something.org"));
    }

    @Test
    public void checkUrlInvalidProtocolTest() throws MalformedURLException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Acceptable protocols: " + ACCEPTABLE_PROTOCOLS);
        checkUrl(new URL("https://something.org"));
    }

    @Test
    public void getPageTest() throws IOException {
        String page = getPage(new URL("http://example.com"));
        Assert.assertEquals(page.length(), 1269);
    }

    @Test
    public void getWordsListTest() throws IOException {
        //
    }
}
