package org.example.simulation;

import org.example.tools.DrawingPanel;

public class RobotCom extends GameParticipant {
    private final SharedMemory memory;

    public RobotCom(String id, int r, int c, DrawingPanel canvas, SharedMemory memory) {
        super(id, r, c, canvas);
        this.memory = memory;
    }

    public void run() {
        while (canvas.isGameRunning()) {
            checkBunnyProximity();
            int[] move = getStrategicMove();
            if (move != null) {
                if (canvas.tryMove(r, c, move[0], move[1], id)) {
                    r = move[0];
                    c = move[1];
                }
            }
            try { Thread.sleep(400); } catch (Exception e) { break; }
        }
    }

    private void checkBunnyProximity() {
        int[] bPos = canvas.getBunnyPosition();
        if (bPos != null) {
            if (Math.abs(r - bPos[0]) <= 2 && Math.abs(c - bPos[1]) <= 2) {
                memory.updateBunny(bPos[0], bPos[1]);
            }
        }
    }

    private int[] getStrategicMove() {
        int targetR = memory.getBunnyR();
        int targetC = memory.getBunnyC();
        if (targetR != -1 && targetC != -1) {
            int[] move = canvas.getMoveTowards(r, c, targetR, targetC);
            if (move != null) return move;
        }
        return canvas.getRandomValidMove(r, c);
    }
}