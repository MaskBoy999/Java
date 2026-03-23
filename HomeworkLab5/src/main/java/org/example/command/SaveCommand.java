package org.example.command;

import org.example.repository.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveCommand implements Command {
    private Repository repo;
    private String path;

    public SaveCommand(Repository repo, String path) {
        this.repo = repo;
        this.path = path;
    }

    @Override
    public void execute() throws org.example.exception.RepositoryException {
        try (var oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(repo);
        } catch (IOException e) {
            throw new org.example.exception.RepositoryException(e);
        }
    }
}