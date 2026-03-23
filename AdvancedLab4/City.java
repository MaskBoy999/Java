package org.example;
import java.util.*;

public class City {
    private List<Intersection> intersections = new ArrayList<>();
    private List<Street> streets = new ArrayList<>();

    public void addIntersection(Intersection inter) {
        intersections.add(inter);
    }

    public void addStreet(Street street) {
        streets.add(street);
    }

    public void displayFilteredStreets(int minLength) {
        System.out.println("\n--- Rezultat Filtrare (Streets > " + minLength + "m & joins >= 3 streets) ---");

        streets.stream()
                .filter(s -> s.getLength() > minLength)
                .filter(s -> {
                    long count = streets.stream()
                            .filter(other -> !other.equals(s)) // nu numărăm strada curentă
                            .filter(other -> other.getFrom().equals(s.getFrom()) ||
                                    other.getTo().equals(s.getFrom()) ||
                                    other.getFrom().equals(s.getTo()) ||
                                    other.getTo().equals(s.getTo()))
                            .count();
                    return count >= 3;
                })
                .forEach(System.out::println);
    }

    public List<Intersection> getIntersections() { return intersections; }
    public List<Street> getStreets() { return streets; }
}