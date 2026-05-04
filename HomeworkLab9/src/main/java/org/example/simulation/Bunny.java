package org.example.simulation;

import org.example.tools.DrawingPanel;
import java.util.LinkedList;
import java.util.List;

public class Bunny extends GameParticipant {
    private final SharedMemory memory;
    private final LinkedList recentPositions = new LinkedList<>();
    private final int historyLimit = 4;

    public Bunny(DrawingPanel canvas, int startR, int startC, SharedMemory memory) {
        super("Bunny", startR, startC, canvas);
        this.memory = memory;
        this.delay = 300;
    }

    @Override
    public void run() {
        while (canvas.isGameRunning()) {
            try {
                handlePause();

                if (r == canvas.endR && c == canvas.endC) {
                    canvas.stopGame("The Bunny escaped");
                    break;
                }

                int[] move = getSmartBunnyMove();
                if (move != null) {
                    if (canvas.tryMove(r, c, move[0], move[1], id)) {
                        recentPositions.addFirst(r + "," + c);
                        if (recentPositions.size() > historyLimit) {
                            recentPositions.removeLast();
                        }

                        r = move[0];
                        c = move[1];
                        memory.updateBunny(r, c);
                    }
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private int[] getSmartBunnyMove() {
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        List<int[]> robots = memory.getAllRobotPositions();
        int[] bestMove = null;
        double maxScore = -Double.MAX_VALUE;

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (isValidMove(nr, nc)) {
                double minDistanceToRobot = 100.0;
                if (robots != null && !robots.isEmpty()) {
                    for (int[] robotPos : robots) {
                        double dist = Math.hypot(nr - robotPos[0], nc - robotPos[1]);
                        if (dist < minDistanceToRobot) {
                            minDistanceToRobot = dist;
                        }
                    }
                }

                double dangerPenalty = 0.0;
                if (minDistanceToRobot < 4.0) {
                    dangerPenalty = Math.pow(5.0 - minDistanceToRobot, 4) * 50.0;
                }

                double distToExit = Math.hypot(nr - canvas.endR, nc - canvas.endC);
                double exitBonus = 50.0 / (distToExit + 1.0);

                double historyPenalty = 0.0;
                if (recentPositions.contains(nr + "," + nc)) {
                    historyPenalty = 30.0;
                }

                double currentScore = exitBonus - dangerPenalty - historyPenalty;

                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    bestMove = new int[]{nr, nc};
                }
            }
        }
        return bestMove != null ? bestMove : canvas.getRandomValidMove(r, c);
    }

    private boolean isValidMove(int nr, int nc) {
        if (nr < 0 || nr >= canvas.getRows() || nc < 0 || nc >= canvas.getCols()) return false;
        return !canvas.isWallBetween(r, c, nr, nc);
    }
}