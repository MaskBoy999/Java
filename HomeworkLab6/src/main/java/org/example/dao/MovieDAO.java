package org.example.dao;
import org.example.connectionpool.DatabaseManager;
import org.example.OOP.*;

import java.sql.*;


public class MovieDAO {
    private Connection con;

    public MovieDAO(Connection con) { this.con = con; }

    public void create(int id, String title, Date releaseDate, int duration, double score, int genreId) throws SQLException {
        String sql = "INSERT INTO movies (id, title, release_date, duration, score, genre_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, title);
            pstmt.setDate(3, releaseDate);
            pstmt.setInt(4, duration);
            pstmt.setDouble(5, score);
            pstmt.setInt(6, genreId);
            pstmt.executeUpdate();
        }
    }

    public void addActorToMovie(int movieId, int actorId) throws SQLException {
        String sql = "INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, actorId);
            pstmt.executeUpdate();
        }
    }
}