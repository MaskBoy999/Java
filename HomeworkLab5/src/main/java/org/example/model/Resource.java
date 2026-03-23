package org.example.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Resource implements Serializable {
    protected String id;
    protected String title;
    protected String location; // Path local sau URL
    protected Map<String, Object> tags = new HashMap<>();

    public Resource(String id, String title, String location) {
        this.id = id;
        this.title = title;
        this.location = location;
    }

    // Getters / Setters
    public String getLocation() { return location; }
    public String getTitle() { return title; }

    public String getId() { return id; }

    @Override
    public String toString() {
        return "Resource{id='" + id + "', title='" + title + "'}";
    }
}