package com.example.greentrip;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlHandling {

    /**
     * get the url contents in a string.
     *
     * @param stringUrl string url
     * @return string contents of url
     * @throws IOException if invalid url
     */
    public static String getUrlContents(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            return readUrl(url);
        } catch (IOException err) {
            err.printStackTrace();
            return null;
        }
    }


    /**
     * Read the contents of the url into a string.
     *
     * @param url
     * @return url contents
     */
    private static String readUrl(URL url){
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder contents = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) contents.append(inputLine);
            return contents.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
