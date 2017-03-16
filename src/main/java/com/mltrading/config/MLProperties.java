package com.mltrading.config;

import com.google.inject.Singleton;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by gmo on 02/02/2017.
 */
@Singleton
public class MLProperties {

    static public Properties prop = new Properties();

    public static void load() {
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }

    public static int getProperty(String key, int defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : new Integer(val);
    }

    public static List<String> getProperties(String key) {
        List<String> l = new ArrayList<>();

        return l;
    }

}
