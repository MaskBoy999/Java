package org.example.dao;
import org.example.connectionpool.DatabaseManager;
import org.example.OOP.*;

import java.sql.*;

public class GenreDAO {
    private Connection con;

    public GenreDAO(Connection con) { this.con = con; }

    public void create(int id, String name) throws SQLException {
        String sql = "INSERT INTO genres (id, name) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        }
    }

    public Genre findByName(String name) throws SQLException {
        String sql = "SELECT * FROM genres WHERE name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return new Genre(rs.getInt("id"), rs.getString("name"));
            }
        }
        return null;
    }
}