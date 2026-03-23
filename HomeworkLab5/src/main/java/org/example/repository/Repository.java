package org.example.repository;

import org.example.exception.RepositoryException;
import org.example.model.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repository implements Serializable {
    private List<Resource> resources = new ArrayList<>();

    public void add(Resource res) {
        resources.add(res);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public static Repository load(String path) throws RepositoryException {
        try (var ois = new ObjectInputStream(new FileInputStream(path))) {
            return (Repository) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Nu s-a putut incarca catalogul: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Repository{resources=" + resources + "}";
    }
}