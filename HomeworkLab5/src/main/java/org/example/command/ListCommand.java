package org.example.command;

import org.example.repository.Repository;

public class ListCommand implements Command {
    private Repository repo;

    public ListCommand(Repository repo) { this.repo = repo; }

    @Override
    public void execute() {
        System.out.println("--- Catalog Resources ---");
        repo.getResources().forEach(System.out::println);
    }
}