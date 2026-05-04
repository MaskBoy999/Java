package org.example.simulation;

import org.example.tools.DrawingPanel;

public class GameManager extends Thread {
    private final DrawingPanel canvas;
    private final SharedMemory memory;
    private final long timeLimitMs;
    private final long startTime;

    public GameManager(DrawingPanel canvas, SharedMemory memory, long timeLimitMs) {
        this.canvas = canvas;
        this.memory = memory;
        this.timeLimitMs = timeLimitMs;
        this.startTime = System.currentTimeMillis();
        setDaemon(true);
    }

    public void run() {
        while (canvas.isGameRunning()) {
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.println("Timp: " + (elapsed / 1000) + "s. Explorat: " + memory.getExploredCount());

            if (elapsed >= timeLimitMs) {
                canvas.stopGame("Limita de timp a expirat.");
                break;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}