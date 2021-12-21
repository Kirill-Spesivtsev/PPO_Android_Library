package com.example.lab_2_library;

public class Book {

    int id;
    String title;
    String author;
    int year;
    String publisher;
    int pageCount;

    public Book(int id, String title, String author, int year, String publisher, int pageCount)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.pageCount = pageCount;
    }

    public Book(){}
}
