package org.example.command;

import freemarker.template.*;
import org.example.repository.Repository;
import java.io.*;
import java.util.*;
import java.awt.Desktop;

public class ReportCommand implements Command {
    private Repository repo;

    public ReportCommand(Repository repo) { this.repo = repo; }

    @Override
    public void execute() throws org.example.exception.RepositoryException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        cfg.setDefaultEncoding("UTF-8");
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));

        try {
            cfg.setDirectoryForTemplateLoading(new File("templates"));
            Template temp = cfg.getTemplate("report.ftl");

            Map<String, Object> root = new HashMap<>();
            root.put("resources", repo.getResources());

            try (Writer out = new FileWriter("report.html")) {
                temp.process(root, out);
            }
            Desktop.getDesktop().open(new File("report.html"));
        } catch (Exception e) {
            throw new org.example.exception.RepositoryException(e);
        }
    }
}