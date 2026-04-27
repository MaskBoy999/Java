package org.example.simulation;

import org.example.tools.*;

public abstract class GameParticipant implements Runnable {
    protected int r, c;
    protected String id;
    protected DrawingPanel canvas;

    public GameParticipant(String id, int startR, int startC, DrawingPanel canvas) {
        this.id = id;
        this.r = startR;
        this.c = startC;
        this.canvas = canvas;
        canvas.forceOccupancy(startR, startC, id);
    }
}