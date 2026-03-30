package com.example.CompulsoryLab7.model;

import java.sql.Date;
import java.util.Objects;

public class Movie {
    private String title;
    private Date releaseDate;
    private double score;
    private String genre;

    public Movie(String title, Date releaseDate, double score, String genre) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.score = score;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public Date getReleaseDate() { return releaseDate; }
    public double getScore() { return score; }
    public String getGenre() { return genre; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}