package org.example;

import com.github.javafaker.Faker;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.spanning.KruskalMinimumSpanningTree;
import org.graph4j.Edge;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();
        City city = new City();

        //nume de alea faker
        List<Intersection> nodes = IntStream.range(0, 10)
                .mapToObj(i -> new Intersection(faker.address().city() + " " + i))
                .collect(Collectors.toList());
        nodes.forEach(city::addIntersection);

        //nume fake, lungimi random
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            Intersection from = nodes.get(rand.nextInt(10));
            Intersection to = nodes.get(rand.nextInt(10));
            if (!from.equals(to)) {
                city.addStreet(new Street(faker.address().streetName(), 100 + rand.nextInt(900), from, to));
            }
        }

        //afisare sortata la strazi dupa lungime
        city.getStreets().sort((s1, s2) -> Integer.compare(s1.getLength(), s2.getLength()));
        System.out.println("Toate strazile (sortate):");
        city.getStreets().forEach(System.out::println);

        //filtrarea
        city.displayFilteredStreets(300);

        //algoritmul care nu numai da minimum cost solution dar si celelate in rank
        findMultipleSpanningTrees(city, 8);
    }

    public static void findMultipleSpanningTrees(City city, int numberOfSolutions) {
        System.out.println("\n--- Top " + numberOfSolutions + " Spanning Tree Solutions ---");

        List<Intersection> allIntersections = city.getIntersections();
        Map<Intersection, Integer> idMap = new HashMap<>();

        for (int i = 0; i < allIntersections.size(); i++) {
            idMap.put(allIntersections.get(i), i);
        }

        var builder = org.graph4j.GraphBuilder.numVertices(allIntersections.size());

        for (Street s : city.getStreets()) {
            Integer u = idMap.get(s.getFrom());
            Integer v = idMap.get(s.getTo());
            builder.addEdge((int)u, (int)v);
        }

        org.graph4j.Graph graph = builder.buildGraph();

        for (Street s : city.getStreets()) {
            Integer u = idMap.get(s.getFrom());
            Integer v = idMap.get(s.getTo());
            graph.setEdgeWeight(u, v, (double) s.getLength());
        }

        for (int i = 1; i <= numberOfSolutions; i++) {
            var mstAlg = new org.graph4j.spanning.KruskalMinimumSpanningTree(graph);
            var spanningTree = mstAlg.getTree();

            // verificam daca ajunge in taote intersectiile vazand daca avem nr intersectii-1 muchii in arbore
            if (spanningTree == null || spanningTree.numEdges() < allIntersections.size() - 1) {
                System.out.println("S-a oprit: Nu mai exista un drum care sa lege TOATE intersectiile.");
                break;
            }

            double totalWeight = 0;
            org.graph4j.Edge edgeToRemove = null;

            for (org.graph4j.Edge e : spanningTree.edges()) {
                totalWeight += e.weight();
                // scoatem muchia cu cost minim
                if (edgeToRemove == null || e.weight() < edgeToRemove.weight()) {
                    edgeToRemove = e;
                }
            }

            System.out.println("Solutia #" + i + " (Cost Total: " + totalWeight + ")");

            if (edgeToRemove != null) {
                graph.removeEdge(edgeToRemove.source(), edgeToRemove.target());
            }
        }
    }
}