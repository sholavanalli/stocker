package com.stocker.scraper;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.stocker.scraper.util.Browser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shashank Raj Holavanalli (sholvanalli@gmail.com)
 */
public class StreetInsider implements Scrapable {

    private static final Logger log = Logger.getLogger(StreetInsider.class.getName());
    private static final String BASE_URL = "http://www.streetinsider.com/rating_history.php?q=";

    private String scrape(String url) {

        HtmlPage page = Browser.getHtmlPage(url);
        log.log(Level.INFO, "Starting scraping: " + url);
        StringBuilder data = new StringBuilder("No data. Something went wrong !!!");
        if (page != null) {
            HtmlDivision overallSummary = null, summary = null;
            List overallSummaries = page.getByXPath("//*[@id=\"content\"]/div[3]");
            List summaries = page.getByXPath("//*[@id=\"content\"]/div[1]");
            if (overallSummaries != null && overallSummaries.size() > 0) {
                overallSummary = (HtmlDivision) overallSummaries.get(0);
            }
            if (summaries != null && summaries.size() > 0) {
                summary = (HtmlDivision) summaries.get(0);
            }
            if (overallSummary != null || summary != null) {
                data.delete(0, data.length());
            }
            if (overallSummary != null) {
                data.append(overallSummary.asText() + "\n");
            }
            if (summary != null) {
                data.append(summary.asText() + "\n");
            }
        }
        return data.toString();
    }

    @Override
    public String getScrapedData(String[] tickerSymbols) {

        StringBuilder data = new StringBuilder();
        for (String oneStock : tickerSymbols) {
            data.append(scrape(BASE_URL + oneStock) + "\n-----------------------------------------\n");
            try {
                // Wait 10 seconds.
                Thread.sleep(1000 * 5);
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            }
        }
        return data.toString();
    }
}
