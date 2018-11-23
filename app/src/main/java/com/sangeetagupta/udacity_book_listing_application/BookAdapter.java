package com.sangeetagupta.udacity_book_listing_application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sangeetagupta1998 on 11/24/18.
 */

public class BookAdapter extends ArrayAdapter<BookItem>{

    public BookAdapter(@NonNull Context context, ArrayList<BookItem> places) {
        super(context, 0, places);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_list_item, parent, false);

        }

        BookItem bookItem = (BookItem) getItem(position);

        TextView bookNameView = listItemView.findViewById(R.id.book_name);
        bookNameView.setText(bookItem.getBookName());

        TextView bookAuthorNameView = listItemView.findViewById(R.id.author_name);
        bookAuthorNameView.setText(bookItem.getBookAuthorName());

        return listItemView;
    }

}
