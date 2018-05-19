package com.plynko;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sun.plugin.dom.exception.WrongDocumentException;

import java.io.IOException;
import java.util.Map;

import static com.plynko.Page.ACCEPTABLE_PROTOCOLS;
import static org.junit.Assert.assertEquals;

public class PageTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void getContentTest() throws IOException {
        // act
        Page page = new Page("http://example.com");

        assertEquals(1270, page.getContent().length());
    }

    @Test
    public void newPageInvalidProtocolTest() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Acceptable protocols: " + ACCEPTABLE_PROTOCOLS);

        // act
        new Page("https://something.org");
    }

    @Test
    public void getNonHtmlContentTest() throws IOException {
        String urlString = "http://humanstxt.org/humans.txt";
        thrown.expect(WrongDocumentException.class);
        thrown.expectMessage(String.format("This URL – %s - does not contain HTML content", urlString));
        Page nonHtmlPage = new Page(urlString);

        // act
        nonHtmlPage.getContent();
    }

    @Test
    public void getWordsListTest() throws IOException {
        Page page = new Page("http://example.com");

        // act
        int actual = page.getWordsNumber();

        assertEquals(32, actual);
    }

    @Test
    public void getWordsSortedMapTest() throws IOException {
        Page page = new Page("http://example.com");

        // act
        Map<String, Long> actual = page.getWordsSortedMap();

        String expected = "{Domain=2, Example=2, More=1, This=1, You=1, asking=1, be=1, coordination=1, documents=1, domain=2, established=1, examples=2, for=2, illustrative=1, in=2, information=1, is=1, may=1, or=1, permission=1, prior=1, this=1, to=1, use=1, used=1, without=1}";
        assertEquals(expected, actual.toString());
    }
}
