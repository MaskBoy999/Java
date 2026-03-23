package org.example;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import org.example.dao.GenreDAO;
import org.example.database.Database;

public class Main {
    public static void main(String[] args) {
        try {
            initDatabase();

            GenreDAO genres = new GenreDAO();
            genres.create(1, "Action");
            genres.create(2, "Drama");

            System.out.println("Gen găsit după ID 1: " + genres.findById(1));
            System.out.println("ID pentru 'Drama': " + genres.findByName("Drama"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.close();
        }
    }

    private static void initDatabase() throws SQLException {
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS genres (id INT PRIMARY KEY, name VARCHAR(50))");
            stmt.execute("CREATE TABLE IF NOT EXISTS movies (id INT PRIMARY KEY, title VARCHAR(100), genre_id INT REFERENCES genres(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS actors (id INT PRIMARY KEY, name VARCHAR(100))");
            stmt.execute("CREATE TABLE IF NOT EXISTS movie_actors (movie_id INT REFERENCES movies(id), actor_id INT REFERENCES actors(id), PRIMARY KEY (movie_id, actor_id))");

            System.out.println("Structura tabelelor a fost verificată/creată.");
        }
    }
}