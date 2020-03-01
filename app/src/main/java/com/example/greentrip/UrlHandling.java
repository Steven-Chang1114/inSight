package com.example.greentrip;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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
            String output = new GetJSONTask().execute(url).get();
            return output;
        } catch (IOException err) {
            err.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
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

    private static class GetJSONTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... url) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url[0].openStream()))) {
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
}