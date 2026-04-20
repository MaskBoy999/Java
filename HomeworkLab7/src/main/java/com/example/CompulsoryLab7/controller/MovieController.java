package com.example.CompulsoryLab7.controller;

import com.example.CompulsoryLab7.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;



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

    //adaugare film
    @PostMapping
    public String addMovie(@RequestBody Movie movie) throws SQLException {
        int nextId = 1;
        String findMaxIdSql = "SELECT MAX(id) FROM movies";

        try (Connection con = dataSource.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(findMaxIdSql)) {
            if (rs.next()) {
                nextId = rs.getInt(1) + 1;
            }
        }

        String insertSql = "INSERT INTO movies (id, title, release_date, score) VALUES (?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            pstmt.setInt(1, nextId);
            pstmt.setString(2, movie.getTitle());
            pstmt.setDate(3, movie.getReleaseDate());
            pstmt.setDouble(4, movie.getScore());
            pstmt.executeUpdate();
            return "Movie added with ID: " + nextId;
        }
    }

    //modificarte completa film
    @PutMapping("/{title}")
    public String updateMovie(@PathVariable String title, @RequestBody Movie movie) throws SQLException {
        String sql = "UPDATE movies SET release_date = ?, score = ? WHERE title = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setDate(1, movie.getReleaseDate());
            pstmt.setDouble(2, movie.getScore());
            pstmt.setString(3, title);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? "Movie updated!" : "Movie not found.";
        }
    }

    //schimbare scor
    @PatchMapping("/{title}/score")
    public String updateScore(@PathVariable String title, @RequestParam double score) throws SQLException {
        String sql = "UPDATE movies SET score = ? WHERE title = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setDouble(1, score);
            pstmt.setString(2, title);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? "Score updated!" : "Movie not found.";
        }
    }

    //sterge film
    @DeleteMapping("/{title}")
    public String deleteMovie(@PathVariable String title) throws SQLException {
        String sql = "DELETE FROM movies WHERE title = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, title);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? "Movie deleted!" : "Movie not found.";
        }
    }
}