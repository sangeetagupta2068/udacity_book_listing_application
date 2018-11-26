package com.sangeetagupta.udacity_book_listing_application;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by sangeetagupta1998 on 11/24/18.
 */

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
            Log.i("URL", "url " + url.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        String response = null;
        StringBuilder builder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                response = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return response;
    }

    private QueryUtils() {

    }

    public static ArrayList<BookItem> extractBookItems(String query) {
        if (TextUtils.isEmpty(query)) {
            return null;
        }

        URL url = createUrl(query);
        ArrayList<BookItem> bookItems = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(makeHTTPRequest(url));
            String author = "";
            JSONArray bookItemsArray = root.getJSONArray("items");
            for (int i = 0; i < bookItemsArray.length(); i++) {
                JSONObject currentBookItem = bookItemsArray.getJSONObject(i);
                JSONObject currentBookItemVolumeInfo = currentBookItem.getJSONObject("volumeInfo");
                String currentBookItemName = currentBookItemVolumeInfo.getString("title");
                if (currentBookItemVolumeInfo.has("authors")) {
                    JSONArray authors = currentBookItemVolumeInfo.getJSONArray("authors");
                    if (!currentBookItemVolumeInfo.isNull("authors")) {
                        author = (String) authors.get(0);
                    } else {
                        author = "unknown author";
                    }
                } else {
                    author = "no author information available";
                }

                bookItems.add(new BookItem(currentBookItemName, author));
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, e.getMessage());
        }

        Log.i("book items", bookItems.toString());

        return bookItems;
    }

}
