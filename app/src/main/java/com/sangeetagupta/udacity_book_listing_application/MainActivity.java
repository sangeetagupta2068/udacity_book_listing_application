package com.sangeetagupta.udacity_book_listing_application;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookItem>> {

    private static String JSON_QUERY;

    BookAdapter bookAdapter;

    EditText searchText;
    Button searchButton;
    TextView textView;
    View spinner;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("https://www.googleapis.com/books/v1/volumes?q=").append(searchText.getText()).append("&maxResults=12");
                JSON_QUERY = stringBuilder.toString();

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
            }
        });
    }

    public void initializeViews() {
        searchText = findViewById(R.id.search);
        searchButton = findViewById(R.id.search_button);
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
    public void onLoadFinished(Loader<List<BookItem>> loader, List<BookItem> bookItems) {
        spinner.setVisibility(View.GONE);

        bookAdapter.clear();

        if (bookItems != null) {
            bookAdapter.addAll(bookItems);
        } else {
            textView.setText("No results matched.");
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        bookAdapter.clear();
    }
}
