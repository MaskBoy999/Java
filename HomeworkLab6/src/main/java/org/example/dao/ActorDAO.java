package org.example.dao;
import org.example.connectionpool.DatabaseManager;
import org.example.OOP.*;

import java.sql.*;

public class ActorDAO {
    private Connection con;

    public ActorDAO(Connection con) { this.con = con; }

    public void create(int id, String name) throws SQLException {
        String sql = "INSERT INTO actors (id, name) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        }
    }

    public Actor findById(int id) throws SQLException {
        String sql = "SELECT * FROM actors WHERE id = ?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return new Actor(rs.getInt("id"), rs.getString("name"));
            }
        }
        return null;
    }
}