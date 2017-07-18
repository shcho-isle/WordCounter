package com.plynko;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.plynko.Page.ACCEPTABLE_PROTOCOLS;

public class PageTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void getContentTest() throws IOException {
        Page page = new Page("http://example.com");
        Assert.assertEquals(1270, page.getContent().length());
    }

    @Test
    public void newPageInvalidProtocolTest() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Acceptable protocols: " + ACCEPTABLE_PROTOCOLS);
        new Page("https://something.org");
    }

    @Test
    public void getNonHtmlContentTest() throws IOException {
        Page nonHtmlPage = new Page("http://humanstxt.org/humans.txt");
        String content = nonHtmlPage.getContent();
        Assert.assertNull(content);
    }

    @Test
    public void getWordsListTest() throws IOException {
        Page page = new Page("http://example.com");
        List<String> actual = page.getWordsList();

        String expected = "[Example, Domain, Example, Domain, This, domain, is, established, to, be, used, for, illustrative, examples, in, documents, You, may, use, this, domain, in, examples, without, prior, coordination, or, asking, for, permission, More, information]";

        Assert.assertEquals(expected, actual.toString());
    }

    @Test
    public void getWordsSortedMapTest() throws IOException {
        Page page = new Page("http://example.com");
        Map<String, Long> actual = page.getWordsSortedMap();

        String expected = "{Domain=2, Example=2, More=1, This=1, You=1, asking=1, be=1, coordination=1, documents=1, domain=2, established=1, examples=2, for=2, illustrative=1, in=2, information=1, is=1, may=1, or=1, permission=1, prior=1, this=1, to=1, use=1, used=1, without=1}";

        Assert.assertEquals(expected, actual.toString());
    }
}
