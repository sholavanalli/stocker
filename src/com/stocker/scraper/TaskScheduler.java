package com.stocker.scraper;

import com.stocker.scraper.notification.Email;
import com.stocker.scraper.util.AppConfig;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Shashank Raj Holavanalli (sholavanalli@gmail.com)
 */
public class TaskScheduler {

    private static final Logger log = Logger.getLogger(TaskScheduler.class.getName());
    Timer timer;

    public void schedule() {
        timer = new Timer("StockMeBaby", false); // Timer thread will be a daemon thread.
        // Repeat every 24 hours starting from 3AM next morning.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        timer.schedule(new EmailTask(), calendar.getTime(), 1000 * 60 * 60 * 24);
    }

    class EmailTask extends TimerTask {

        private int emailCount = 0;

        @Override
        public void run() {

            String scrapers = AppConfig.getProperties().getProperty("scrapers");
            String tickers = AppConfig.getProperties().getProperty("ticker_symbols");
            String sender = AppConfig.getProperties().getProperty("sender");
            String pw = AppConfig.getProperties().getProperty("pw");
            String recipients = AppConfig.getProperties().getProperty("recipients");

            String[] to = recipients.contains(",") ? recipients.split(",") : new String[]{recipients};
            String[] tickerSymbols = tickers.contains(",") ? tickers.split(",") : new String[]{tickers};
            String[] scraperClassNames = scrapers.contains(",") ? scrapers.split(",") : new String[]{scrapers};
            StringBuilder sb = new StringBuilder();
            for (String scraperClassName : scraperClassNames) {
                try {
                    Scrapable scrapable = (Scrapable) Class.forName("com.stockmebaby.scraper." + scraperClassName).newInstance();
                    sb.append(scrapable.getScrapedData(tickerSymbols));
                } catch (Exception e) {
                    log.log(Level.SEVERE, e.getMessage());
                    e.printStackTrace();
                }
            }
            Email.send(sb.toString(), emailCount++, sender, pw, to);
        }
    }

    public static void main(String[] args) {
        TaskScheduler ts = new TaskScheduler();
        ts.schedule();
    }
}
