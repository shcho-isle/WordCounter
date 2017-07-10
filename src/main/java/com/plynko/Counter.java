package com.plynko;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.plynko.StringUtils.*;

public class Counter {
    private static final Logger LOG;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
        LOG = Logger.getLogger(Counter.class.getName());
        FileHandler fh;
        try {
            fh = new FileHandler("wordCounter.log", true);
            fh.setFormatter(new SimpleFormatter());
            LOG.addHandler(fh);
        } catch (IOException e) {
            System.err.println("Cannot create log file: ");
            e.printStackTrace();
        }
        LOG.setLevel(Level.INFO);
        LOG.setUseParentHandlers(false);
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No URL specified.");
        }
        URL url = new URL(args[0]);
        checkUrl(url);
        String page = getPage(url);

        List<String> wordsList = getWordsList(page);
        System.out.println("Total number of words is: " + wordsList.size());

        Map<String, Long> wordsSortedMap = getWordsSortedMap(wordsList);
        wordsSortedMap.forEach((k, v) -> System.out.println(v + "\t" + k));
        LOG.info("words: " + wordsList.size() + ", unique words: " + wordsSortedMap.size() + ", url: " + args[0]);
    }
}