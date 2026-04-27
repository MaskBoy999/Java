package org.example.simulation;

import org.example.tools.DrawingPanel;

public class Bunny extends GameParticipant {
    public Bunny(DrawingPanel canvas, int startR, int startC) {
        super("Bunny", startR, startC, canvas);
    }

    public void run() {
        while (canvas.isGameRunning()) {
            if (r == canvas.endR && c == canvas.endC) {
                canvas.stopGame("The Bunny escaped :)");
                break;
            }
            int[] move = canvas.getRandomValidMove(r, c);
            if (move != null) {
                if (canvas.tryMove(r, c, move[0], move[1], id)) {
                    r = move[0];
                    c = move[1];
                }
            }
            try { Thread.sleep(300); } catch (InterruptedException e) { break; }
        }
    }
}