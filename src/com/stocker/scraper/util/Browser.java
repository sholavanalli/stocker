package com.stocker.scraper.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Shashank Raj Holavanalli (sholavanalli@gmail.com)
 */
public class Browser {

    private static final Logger log = Logger.getLogger(Browser.class.getName());
    private static WebClient webClient;

    private Browser() {
        // This utility class should not be instantiated.
    }

    /**
     * Return the html web page given the url.
     *
     * @param url - the url of the web page.
     * @return - the web page or null if not found.
     */
    public static HtmlPage getHtmlPage(String url) {

        if (webClient == null) {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(false);
        }
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);

        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
        return page;
    }
}
