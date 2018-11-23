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

    private static URL createUrl(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,e.getMessage());
        }
        return url;
    }

    private static String makeHTTPRequest(URL url){
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        }catch (IOException e){
            Log.e(LOG_TAG,e.getMessage());
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG,e.getMessage());
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream){
        String response = null;
        StringBuilder builder = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line!=null){
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

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(TextUtils.isEmpty(query)){
            return null;
        }

        URL url = createUrl(query);
        ArrayList<BookItem> bookItems = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(makeHTTPRequest(url));
            JSONArray bookItemsArray = root.getJSONArray("items");
            for(int i = 0; i <bookItemsArray.length(); i++){
                JSONObject currentBookItem = bookItemsArray.getJSONObject(i);
                String currentBookItemName = currentBookItem.getString("title");
                JSONArray authorNames = currentBookItem.getJSONArray("authors");
                StringBuilder builder = new StringBuilder();
                for(int j = 0; i < authorNames.length(); j++){
                    builder.append(authorNames.getString(j) + ",");
                }
                bookItems.add(new BookItem(currentBookItemName,builder.toString()));
            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, e.getMessage());
        }

        return bookItems;
    }

}
