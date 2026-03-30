package org.example.OOP;


import java.sql.Date;


public class Movie {
    private int id;
    private String title;
    private Date releaseDate;
    private int duration;
    private double score;
    private int genreId;

    public Movie(int id, String title, Date releaseDate, int duration, double score, int genreId) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
        this.genreId = genreId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public Date getReleaseDate() { return releaseDate; }
    public int getDuration() { return duration; }
    public double getScore() { return score; }
    public int getGenreId() { return genreId; }
}