package org.example.simulation;

import org.example.tools.DrawingPanel;

public abstract class GameParticipant implements Runnable {
    protected int r, c;
    protected String id;
    protected DrawingPanel canvas;
    protected volatile boolean paused = false;
    protected volatile int delay = 400;

    public GameParticipant(String id, int startR, int startC, DrawingPanel canvas) {
        this.id = id;
        this.r = startR;
        this.c = startC;
        this.canvas = canvas;
        canvas.forceOccupancy(startR, startC, id);
    }

    public void setPaused(boolean p) {
        this.paused = p;
    }

    public synchronized void resumeParticipant() {
        this.paused = false;
        notifyAll();
    }

    public void setDelay(int d) {
        this.delay = d;
    }

    protected void handlePause() throws InterruptedException {
        synchronized(this) {
            while (paused && canvas.isGameRunning()) {
                wait();
            }
        }
    }
}