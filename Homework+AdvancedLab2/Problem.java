import java.util.*;

/**
 * Describes the instance of a routing problem
 */

public class Problem{
    private final Set<Location> locations = new HashSet<>();
    private final Set<Road> roads = new HashSet<>();

    /**
     *
     * @param loc param that adds location to the problem
     */

    public void addLocation(Location loc)
    {
        locations.add(loc);
    }

    /**
     *
     * @param road param that adds roads to the problem
     */

    public void addRoad(Road road)
    {
        roads.add(road);
    }

    /**
     * find best route from a starting point to an ending one
     * @param start start point
     * @param end end point
     * @return an object that cotain the list of visited locations
     */

    public Solution solve(Location start, Location end){
        Map<Location, Double> dists = new HashMap<>();
        Map<Location, Location> parent = new HashMap<>();
        PriorityQueue<Location> pq = new PriorityQueue<>(Comparator.comparingDouble(dists::get));

        for (Location l : locations) dists.put(l, Double.MAX_VALUE);
        dists.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()){
            Location curr = pq.poll();
            if (curr.equals(end)) break;

            for (Road r : roads){
                if (r.GetFrom().equals(curr)){
                    if (dists.get(curr) + r.GetLength() < dists.get(r.GetTo())){
                        dists.put(r.GetTo(), dists.get(curr));
                        parent.put(r.GetTo(), curr);
                        pq.add(r.GetTo());
                    }
                }
            }
        }
        return new Solution(reconstructPath(parent, start, end));
    }

    /**
     *  A helper method that backtracks from the end location to the starting one
     * @param p the parent map which when given a location it will say which location is its parent
     * @param s the starting location
     * @param e the end location
     * @return a list of locations representing the path, empty list if there's no path
     */

    private List<Location> reconstructPath(Map<Location, Location> p, Location s, Location e){
        List<Location> path = new ArrayList<>();
        for (Location at = e; at != null; at = p.get(at)) path.add(at);
        Collections.reverse(path);
        return (!path.isEmpty() && path.get(0).equals(s)) ? path : Collections.emptyList();
    }
}

/**
 * data record representing the final path found by the solver
 * @param path the visited city path that needs to be reversed
 */

record Solution(List<Location> path){
    @Override
    public String toString() {
        return path.isEmpty() ? "No route found" : "Best Route: " + path;
    }
}
