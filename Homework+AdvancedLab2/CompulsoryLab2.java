import java.util.Random;

public class CompulsoryLab2{
    public static void main(String[] args){
        Problem problem = new Problem();
        Random rand = new Random();
        int numLocations = 1000;
        int numRoads = 5000;

        Location[] allLocations = new Location[numLocations];
        for (int i = 0; i < numLocations; i++){
            allLocations[i] = new City("City_" + i, rand.nextDouble() * 1000, rand.nextDouble() * 1000, rand.nextInt(1000000));
            problem.addLocation(allLocations[i]);
        }

        for(int i = 0; i < numRoads; i++){
            Location from = allLocations[rand.nextInt(numLocations)];
            Location to = allLocations[rand.nextInt(numLocations)];

            if (!from.equals(to)){
                double dist = Math.sqrt(Math.pow(from.GetLocationX() - to.GetLocationX(), 2) +
                        Math.pow(from.GetLocationY() - to.GetLocationY(), 2));

                problem.addRoad(new Road(RoadType.HIGHWAY, dist, 130, from, to));
            }
        }

        System.out.println("Dijkstra for" + numLocations + " locations");

        long startTime = System.nanoTime();
        Solution sol = problem.solve(allLocations[0], allLocations[numLocations - 1]);
        long endTime = System.nanoTime();

        long durationMicros = (endTime - startTime) / 1000;
        System.out.println("Execution time: " + (durationMicros/1000.0) + " ms");
        System.out.println(sol);

        Runtime runtime = Runtime.getRuntime();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory used: " + (memory / (1024 * 1024)) + " MB");
    }
}