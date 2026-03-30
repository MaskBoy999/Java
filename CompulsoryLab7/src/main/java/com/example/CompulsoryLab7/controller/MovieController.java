package com.example.CompulsoryLab7.controller;

import com.example.CompulsoryLab7.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public List<Movie> getMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT title, release_date, score, genre FROM v_movie_report";

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getString("title"),
                        rs.getDate("release_date"),
                        rs.getDouble("score"),
                        rs.getString("genre")
                ));
            }
        }
        return movies;
    }
}