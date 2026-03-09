package org.example;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Intersection> intersections = IntStream.range(0, 10).mapToObj(i -> new Intersection("v" + i)).collect(Collectors.toList());

        List<Street> streetList = new LinkedList<>();
        streetList.add(new Street("Street A", 500, intersections.get(0), intersections.get(1)));
        streetList.add(new Street("Street B", 200, intersections.get(1), intersections.get(2)));
        streetList.add(new Street("Street C", 800, intersections.get(2), intersections.get(3)));
        streetList.add(new Street("Street D", 100, intersections.get(0), intersections.get(2)));

        streetList.sort((s1, s2) -> Integer.compare(s1.getLength(), s2.getLength()));

        System.out.println("--- Străzi sortate (folosind Integer.compare în Lambda) ---");

        streetList.forEach(s -> System.out.println(s));

        Set<Intersection> intersectionSet = new HashSet<>(intersections);
        boolean added = intersectionSet.add(new Intersection("v0"));

        System.out.println("\n--- Rezultat HashSet ---");
        System.out.println("Mărime set: " + intersectionSet.size());
        System.out.println("A fost adăugat duplicatul 'v0'? " + (added ? "Da" : "Nu"));

    }
}