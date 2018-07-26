package com.khahani.app.quakeevent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dev on 7/24/2018.
 */

public class NetworkUtil {

    public static Event fetchData(String requestURL) {
        //1- Create URL
        URL url = createUrl(requestURL);

        // 2 - get json
        String json = makeHttpRequest(url);

        //3- convert json to event

        Event event = extractFromJson(json);

        return event;
    }

    public static URL createUrl(String requestUrl) {
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String makeHttpRequest(URL url) {
        String json = "";

        if (url == null) {
            return json;
        }

        InputStream inputStream = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                // 3- Convert stream to json
                json = readFromStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    public static Event extractFromJson(String json) {
        Event event = null;

        try {
            JSONObject root = new JSONObject(json);
            JSONArray features = root.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");
            double mag = properties.getDouble("mag");
            int felt = properties.getInt("felt");
            String siteUrl = properties.getString("url");

            event = new Event(mag, felt, siteUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return event;
    }


}
