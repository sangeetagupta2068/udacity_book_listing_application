package com.sangeetagupta.udacity_book_listing_application;

/**
 * Created by sangeetagupta1998 on 11/24/18.
 */

public class BookItem {
    String bookName;
    String bookAuthorName;

    public BookItem(String bookName, String bookAuthorName){
        this.bookName = bookName;
        this.bookAuthorName = bookAuthorName;
    }

    String getBookName(){
        return bookName;
    }

    String getBookAuthorName(){
        return bookAuthorName;
    }
}
