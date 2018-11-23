package com.sangeetagupta.udacity_book_listing_application;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sangeetagupta1998 on 11/24/18.
 */

public class BookItemLoader extends AsyncTaskLoader<List<BookItem>> {

    String url;
    ArrayList<BookItem> bookItems;

    BookItemLoader(Context context, String url){
        super(context);
        this.url = url;
    }
    @Override
    public List<BookItem> loadInBackground() {
        if(url == null){
            return null;
        }
        bookItems = QueryUtils.extractBookItems(url);
        return bookItems;
    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }
}
