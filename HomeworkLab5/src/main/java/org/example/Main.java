package org.example;

import org.example.command.*;
import org.example.model.Book;
import org.example.repository.Repository;
import org.example.exception.RepositoryException;

public class Main {
    public static void main(String[] args) {
        try {
            Repository repo = new Repository();
            repo.add(new Book("erterhh67", "The Art", "https://google.com", "Eduard", 1967));
            repo.add(new Book("seweg25", "Java", "C:\\Users\\Admin\\Documents\\javadoc\\allclasses-index.html", "Daniel", 2005));

            System.out.println("Salvare catalog...");
            Command save = new SaveCommand(repo, "catalog.bin");
            save.execute();

            System.out.println("Incarcare in catalog ca obiect nou...");
            Repository loadedRepo = Repository.load("catalog.bin"); // Metodă statică în Repository

            Command list = new ListCommand(loadedRepo);
            list.execute();

            System.out.println("Generare raport HTML...");
            Command report = new ReportCommand(loadedRepo);
            report.execute();

            if (!loadedRepo.getResources().isEmpty()) {
                System.out.println("Deschidere resursa...");
                Command view = new ViewCommand(loadedRepo.getResources().get(0));
                view.execute();
            }

        } catch (RepositoryException e) {
            System.err.println("Eroare aplicatie: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}