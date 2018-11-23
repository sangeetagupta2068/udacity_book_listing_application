package com.sangeetagupta.udacity_book_listing_application;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static String JSON_QUERY;

    ArrayList<BookItem> bookItems;
    BookAdapter bookAdapter;

    SearchView searchView;
    TextView textView;
    View spinner;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchView.setSubmitButtonEnabled(false);
                s = s.trim();
                s = s.toLowerCase().replaceAll(" ","+");
                JSON_QUERY = "https://www.googleapis.com/books/v1/volumes?q=search+" + s;
                textView.setText(JSON_QUERY);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (!isConnected) {
                    spinner.setVisibility(View.GONE);
                    textView.setText("No internet connection");
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(1, null, MainActivity.this);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchView.setSubmitButtonEnabled(true);
                return false;
            }
        });
    }

    public void initializeViews() {
        searchView = findViewById(R.id.search);
        textView = findViewById(R.id.search_status_message);
        spinner = findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.INVISIBLE);
        listView = findViewById(R.id.list_of_book);

        bookAdapter = new BookAdapter(this, new ArrayList<BookItem>());
        listView.setAdapter(bookAdapter);

        listView.setEmptyView(findViewById(R.id.textView));
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new BookItemLoader(this, JSON_QUERY);
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        spinner.setVisibility(View.GONE);
        textView.setText("No results matched.");
        bookAdapter.clear();
        if (bookItems != null) {
            bookAdapter.addAll(bookItems);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        bookAdapter.clear();
    }
}
