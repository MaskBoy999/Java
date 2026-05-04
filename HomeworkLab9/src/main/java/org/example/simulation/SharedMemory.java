package org.example.simulation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

public class SharedMemory {
    private final Map<String, Boolean> exploredMap = new ConcurrentHashMap<>();
    private volatile int bunnyR = -1;
    private volatile int bunnyC = -1;

    private final Map<String, int[]> robotPositions = new ConcurrentHashMap<>();

    public void updateRobotPosition(String id, int r, int c) {
        robotPositions.put(id, new int[]{r, c});
    }

    public List<int[]> getAllRobotPositions() {
        return new ArrayList<>(robotPositions.values());
    }

    public void markExplored(int r, int c) {
        exploredMap.put(r + "," + c, true);
    }

    public boolean isExplored(int r, int c) {
        return exploredMap.containsKey(r + "," + c);
    }

    public int getExploredCount() {
        return exploredMap.size();
    }

    public void updateBunny(int r, int c) {
        this.bunnyR = r;
        this.bunnyC = c;
    }

    public int getBunnyR() { return bunnyR; }
    public int getBunnyC() { return bunnyC; }
}