package com.stocker.scraper;

/**
 * @author Shashank Raj Holavanalli (sholavanalli@gmail.com)
 */
public interface Scrapable {

    /**
     * Get data for the given ticker symbols from the web scraper.
     * @param tickerSymbols - array of ticker symbols.
     * @return - data scraped by the web scraper.
     */
    public String getScrapedData(String[] tickerSymbols);
}
