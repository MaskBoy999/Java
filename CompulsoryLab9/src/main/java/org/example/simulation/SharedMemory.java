package org.example.simulation;

public class SharedMemory {
    private volatile int bunnyR = -1;
    private volatile int bunnyC = -1;

    public synchronized void updateBunny(int r, int c) {
        bunnyR = r;
        bunnyC = c;
    }

    public int getBunnyR() { return bunnyR; }
    public int getBunnyC() { return bunnyC; }
}