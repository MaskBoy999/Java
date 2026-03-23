package org.example;

import com.github.javafaker.Faker;
import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.spanning.KruskalMinimumSpanningTree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();
        City city = new City();
        Random rand = new Random();

        List<Intersection> nodes = IntStream.range(0, 10)
                .mapToObj(i -> new Intersection(faker.address().city() + " " + i))
                .collect(Collectors.toList());
        nodes.forEach(city::addIntersection);

        for (int i = 0; i < 30; i++) {
            Intersection from = nodes.get(rand.nextInt(nodes.size()));
            Intersection to = nodes.get(rand.nextInt(nodes.size()));

            if (!from.equals(to)) {
                // calcul distanta ca sa respecte inegalitatea triunghiului
                double distance = Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
                city.addStreet(new Street(faker.address().streetName(), distance, from, to));
            }
        }

        // filtrarea
        city.getStreets().sort(Comparator.comparingDouble(Street::getLength));
        System.out.println("Toate străzile (sortate după distanța euclidiană):");
        city.getStreets().forEach(System.out::println);

        city.displayFilteredStreets(500);

        // minimum cost si restul
        findMultipleSpanningTrees(city, 8);

        // ruta de mentenanta (wip)
        findMaintenanceRoute(city);
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

    public static void findMaintenanceRoute(City city) {
        System.out.println("\n--- Advanced: Real-Street Maintenance Route ---");

        List<Intersection> allIntersections = city.getIntersections();
        Map<Intersection, Integer> idMap = new HashMap<>();
        for (int i = 0; i < allIntersections.size(); i++) {
            idMap.put(allIntersections.get(i), i);
        }

        var builder = org.graph4j.GraphBuilder.numVertices(allIntersections.size());
        for (Street s : city.getStreets()) {
            Integer u = idMap.get(s.getFrom());
            Integer v = idMap.get(s.getTo());
            if (u != null && v != null) {
                builder.addEdge((int)u, (int)v);
            }
        }

        org.graph4j.Graph graph = builder.buildGraph();

        for (Street s : city.getStreets()) {
            Integer u = idMap.get(s.getFrom());
            Integer v = idMap.get(s.getTo());
            if (u != null && v != null) {
                graph.setEdgeWeight(u, v, s.getLength());
            }
        }

        var mstAlg = new org.graph4j.spanning.KruskalMinimumSpanningTree(graph);
        var mst = mstAlg.getTree();

        if (mst == null || mst.numEdges() < allIntersections.size() - 1) {
            System.out.println("Eroare: Nu putem ajunge la toate strazile.");
            return;
        }

        List<Integer> order = new ArrayList<>();
        dfsMst(0, mst, new boolean[allIntersections.size()], order);

        double totalDistance = 0;
        List<String> fullPathNames = new ArrayList<>();

        for (int i = 0; i < order.size(); i++) {
            int u = order.get(i);
            int v = (i == order.size() - 1) ? order.get(0) : order.get(i + 1);

            var dijkstra = new org.graph4j.shortestpath.DijkstraShortestPathHeap(graph, u);
            var path = dijkstra.findPath(v);

            if (path != null) {
                totalDistance += path.computeEdgesWeight();

                if (i == 0) {
                    fullPathNames.add(allIntersections.get(u).getName());
                }

                int[] vertices = path.vertices();
                for (int j = 1; j < vertices.length; j++) {
                    fullPathNames.add(allIntersections.get(vertices[j]).getName());
                }
            }
        }

        System.out.println("Ruta finala:");
        System.out.println(String.join(" -> ", fullPathNames));
        System.out.printf("Distanta totala: %.2f m\n", totalDistance);
    }

    private static void dfsMst(int u, org.graph4j.Graph mst, boolean[] visited, List<Integer> result) {
        visited[u] = true;
        result.add(u);
        for (int v : mst.neighbors(u)) {
            if (!visited[v]) {
                dfsMst(v, mst, visited, result);
            }
        }
    }
}