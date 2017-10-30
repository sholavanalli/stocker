package com.stocker.scraper.util;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shashank Raj Holavanalli (sholavanalli@gmail.com)
 */
public class AppConfig {

    private static final Logger log = Logger.getLogger(AppConfig.class.getName());

    private static final String FILE_NAME = "config.properties";
    private static final Set<String> KEYS = new HashSet<String>();
    private static Properties properties;
    private static FileInputStream fileInputStream;

    static {
        KEYS.add("sender");
        KEYS.add("pw");
        KEYS.add("recipients");
        KEYS.add("ticker_symbols");
        KEYS.add("scrapers");
    }


    private AppConfig() {

    }

    public static Properties getProperties() {

        if (properties == null) {
            try {
                fileInputStream = new FileInputStream(FILE_NAME);
                properties = new Properties();
                properties.load(fileInputStream);
                if (!properties.stringPropertyNames().containsAll(KEYS)) {
                    throw new Exception("The configuration file must contain the following keys: " + KEYS.toString());
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return properties;
    }
}
