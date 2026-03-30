package org.example;

import java.sql.*;

import org.example.connectionpool.DatabaseManager;
import org.example.reportgenerator.ReportGenerator;
import org.example.OOP.*;
import org.example.dao.*;


public class Main {
    public static void main(String[] args) {
        try (Connection con = DatabaseManager.getConnection()) {

            con.setAutoCommit(false);

            try {
                GenreDAO genreDAO = new GenreDAO(con);
                MovieDAO movieDAO = new MovieDAO(con);
                ActorDAO actorDAO = new ActorDAO(con);
                ReportGenerator report = new ReportGenerator(con);

                genreDAO.create(1, "Science Fiction");
                actorDAO.create(50, "Cillian Murphy");
                movieDAO.create(1001, "Oppenheimer", Date.valueOf("2023-07-21"), 180, 8.4, 1);
                movieDAO.addActorToMovie(1001, 50);

                System.out.println("[DB] Date inserate in memorie.");

                Genre g = genreDAO.findByName("Science Fiction");

                if (g != null) {
                    System.out.println("Am extras obiectul din baza de date.");
                    System.out.println("Nume Gen: " + g.getName() + " (ID: " + g.getId() + ")");
                } else {
                    System.out.println("Genul nu a fost găsit.");
                }

                report.generateHtmlReport("raport_test.html");
                System.out.println("[Report] HTML generat cu succes!");

            } catch (Exception e) {
                System.err.println("Eroare in timpul executiei: " + e.getMessage());
            }

            con.rollback();
        } catch (SQLException e) {
            System.err.println("Eroare la conexiunea DB: " + e.getMessage());
        }
    }
}