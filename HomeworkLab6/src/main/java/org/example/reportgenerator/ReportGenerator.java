package org.example.reportgenerator;
import org.example.connectionpool.DatabaseManager;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportGenerator {
    private Connection con;

    public ReportGenerator(Connection con) { this.con = con; }

    public void generateHtmlReport(String filePath) throws Exception {
        StringBuilder html = new StringBuilder("<html><body><table border='1'><tr><th>Film</th><th>Data</th><th>Scor</th><th>Gen</th></tr>");

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM v_movie_report")) {
            while (rs.next()) {
                html.append("<tr>")
                        .append("<td>").append(rs.getString("title")).append("</td>")
                        .append("<td>").append(rs.getDate("release_date")).append("</td>")
                        .append("<td>").append(rs.getBigDecimal("score")).append("</td>")
                        .append("<td>").append(rs.getString("genre")).append("</td>")
                        .append("</tr>");
            }
        }
        html.append("</table></body></html>");
        try (FileWriter fw = new FileWriter(filePath)) { fw.write(html.toString()); }
    }
}