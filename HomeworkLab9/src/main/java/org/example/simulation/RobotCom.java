package org.example.simulation;

import org.example.tools.DrawingPanel;
import java.util.*;

public class RobotCom extends GameParticipant {
    private final SharedMemory memory;
    private int[] lastPosition = {-1, -1};

    public RobotCom(String id, int r, int c, DrawingPanel canvas, SharedMemory memory) {
        super(id, r, c, canvas);
        this.memory = memory;
        memory.markExplored(r, c);
    }

    @Override
    public void run() {
        memory.markExplored(r, c);

        while (canvas.isGameRunning()) {
            try {
                handlePause();
                checkBunnyProximity();

                int[] move = getStrategicMove();
                if (move != null) {
                    if (canvas.tryMove(r, c, move[0], move[1], id)) {
                        this.lastPosition[0] = r;
                        this.lastPosition[1] = c;
                        r = move[0];
                        c = move[1];

                        memory.markExplored(r, c);
                        memory.updateRobotPosition(id, r, c);
                    }
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                break;
            }
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

    private final java.util.LinkedList<String> moveHistory = new java.util.LinkedList<>();

    private int[] getStrategicMove() {
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] bestMove = null;
        boolean foundNew = false;

        List<int[]> shuffledDirs = Arrays.asList(dirs);
        Collections.shuffle(shuffledDirs);

        for (int[] d : shuffledDirs) {
            int nr = r + d[0], nc = c + d[1];

            if (isValidMove(nr, nc)) {
                if (!memory.isExplored(nr, nc)) {
                    return new int[]{nr, nc};
                }

                if (nr != lastPosition[0] || nc != lastPosition[1]) {
                    bestMove = new int[]{nr, nc};
                }
            }
        }

        return (bestMove != null) ? bestMove : canvas.getRandomValidMove(r, c);
    }

    private boolean isValidMove(int nr, int nc) {
        return nr >= 0 && nr < canvas.getRows() && nc >= 0 && nc < canvas.getCols()
                && !canvas.isWallBetween(r, c, nr, nc);
    }
}