package com.example.caesaraudio;

import java.io.Serializable;

public class Audiobook implements Serializable {
    private long id; // will be generated in database
    private String title;
    private String author;
    private String filepath;
    private int durationSeconds; // length of audiobook in seconds
    private String book;
    public Audiobook(){

    }

    public Audiobook(String title, String author, String filepath, int durationSeconds, String book){
        this.title = title;
        this.author = author;
        this.filepath = filepath;
        this.durationSeconds = durationSeconds;
        this.book = book;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

}