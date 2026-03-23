package org.example.model;

public class Book extends Resource {
    private String author;
    private int year;

    public Book(String id, String title, String location, String author, int year) {
        super(id, title, location);
        this.author = author;
        this.year = year;
    }
}