import java.util.*;

public class SocialNetwork {
    private final List<Profile> nodes = new ArrayList<>();

    private int timer;
    private Map<Profile, Integer> discoveryTime = new HashMap<>();
    private Map<Profile, Integer> lowLink = new HashMap<>();
    private Set<Profile> articulationPoints = new HashSet<>();

    private Stack<Edge> edgeStack = new Stack<>();
    private List<Set<Profile>> biconnectedComponents = new ArrayList<>();

    private record Edge(Profile u, Profile v) {}

    public void addProfile(Profile p) { nodes.add(p); }

    public int getImportance(Profile p) {
        return p.getRelationships().size();
    }

    public void printNetworkByImportance() {
        List<Profile> sortedNodes = new ArrayList<>(nodes);

        sortedNodes.sort((p1, p2) -> Integer.compare(getImportance(p2), getImportance(p1)));

        System.out.println("\n--- Social Network (Ordered by Importance) ---");
        for (Profile p : sortedNodes) {
            System.out.println(p.GetName() + " [Importance: " + getImportance(p) +
                    ", Address: " + p.GetAddress() + "]");
        }
    }

    public void analyzeConnectivity() {
        timer = 0;
        discoveryTime.clear();
        lowLink.clear();
        articulationPoints.clear();
        biconnectedComponents.clear();
        edgeStack.clear();

        for (Profile p : nodes) {
            if (!discoveryTime.containsKey(p)) {
                findBCC(p, null);
            }
        }

        printResults();
    }

    private void findBCC(Profile u, Profile p) {
        discoveryTime.put(u, timer);
        lowLink.put(u, timer);
        timer++;
        int children = 0;

        for (Profile v : u.getRelationships().keySet()) {
            if (v.equals(p)) continue;

            if (discoveryTime.containsKey(v)) {
                lowLink.put(u, Math.min(lowLink.get(u), discoveryTime.get(v)));
                if (discoveryTime.get(v) < discoveryTime.get(u)) {
                    edgeStack.push(new Edge(u, v));
                }
            } else {
                children++;
                edgeStack.push(new Edge(u, v));
                findBCC(v, u);
                lowLink.put(u, Math.min(lowLink.get(u), lowLink.get(v)));

                if ((p != null && lowLink.get(v) >= discoveryTime.get(u)) || (p == null && children > 1)) {
                    articulationPoints.add(u);
                    extractComponent(u, v);
                }
            }
        }
    }

    private void extractComponent(Profile u, Profile v) {
        Set<Profile> component = new HashSet<>();
        while (true) {
            Edge e = edgeStack.pop();
            component.add(e.u);
            component.add(e.v);
            if (e.u.equals(u) && e.v.equals(v)) break;
        }
        biconnectedComponents.add(component);
    }

    private void printResults() {
        System.out.println("\n--- Advanced Connectivity Analysis ---");
        System.out.println("Articulation Points: " + articulationPoints.stream().map(Profile::GetName).toList());

        System.out.println("Maximal Biconnected Components (Blocks):");
        for (int i = 0; i < biconnectedComponents.size(); i++) {
            System.out.print("Component " + (i + 1) + ": { ");
            for (Profile p : biconnectedComponents.get(i)) {
                System.out.print(p.GetName() + " ");
            }
            System.out.println("}");
        }
    }

    public Set<Profile> getArticulationPoints() {
        return articulationPoints;
    }

    public List<Set<Profile>> getBiconnectedComponents() {
        return biconnectedComponents;
    }
}