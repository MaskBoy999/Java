package org.example.tools;

import org.example.simulation.*;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class DrawingPanel extends JPanel implements Serializable {
    private final MainFrame frame;
    private int rows, cols;
    private final int canvasWidth = 600;
    private final int canvasHeight = 600;
    private boolean[][] hWalls, vWalls;
    public int startR, startC, endR, endC;
    private final int wallThick = 2;

    private Timer genTimer;
    private Stack<int[]> stack;
    private boolean[][] visitedGen;

    private final Map<Integer, String> cellOccupancy = new ConcurrentHashMap<>();
    private volatile boolean gameRunning = false;
    private final List<GameParticipant> participants = new ArrayList<>();
    private final List<Thread> entityThreads = new ArrayList<>();

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        setBackground(Color.BLACK);
        initMouseListener();
    }

    public boolean isGameRunning() { return gameRunning; }

    public synchronized void startGame() {
        if (gameRunning || hWalls == null) return;
        cellOccupancy.clear();
        participants.clear();
        entityThreads.clear();
        gameRunning = true;

        SharedMemory memory = new SharedMemory();

        Bunny bunny = new Bunny(this, startR, startC, memory);
        participants.add(bunny);
        Thread bThread = new Thread(bunny);
        entityThreads.add(bThread);

        for (int i = 0; i < 3; i++) {
            int[] robStart = getRandomEmptyCell();
            RobotCom robot = new RobotCom("R" + i, robStart[0], robStart[1], this, memory);
            participants.add(robot);
            Thread rThread = new Thread(robot);
            entityThreads.add(rThread);
        }

        for (Thread t : entityThreads) t.start();

        new GameManager(this, memory, 60000).start();

        new InputController(participants, this).start();

        System.out.println("Sistem pornit. Comenzile (stop, resume, slow, fast) sunt active in consola.");
    }

    public synchronized void stopGame(String message) {
        if (!gameRunning) return;
        gameRunning = false;

        for (Thread t : entityThreads) {
            t.interrupt();
        }

        for (GameParticipant p : participants) {
            p.resumeParticipant();
        }

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        });

        System.out.println("\n--- STARE FINALA ---");
        printTextState();
        System.out.println("Rezultat: " + message);
    }

    public void forceOccupancy(int r, int c, String id) {
        cellOccupancy.put(r * cols + c, id);
    }

    public synchronized boolean tryMove(int r, int c, int nextR, int nextC, String entityId) {
        if (!gameRunning) return false;
        if (isWallBetween(r, c, nextR, nextC)) return false;

        int nextHash = nextR * cols + nextC;
        String occupant = cellOccupancy.get(nextHash);

        if (occupant != null) {
            if ((occupant.equals("Bunny") && entityId.startsWith("R")) ||
                    (occupant.startsWith("R") && entityId.equals("Bunny"))) {
                stopGame("Robotii au prins Iepurasul!");
                return true;
            }
            return false;
        }

        cellOccupancy.remove(r * cols + c);
        cellOccupancy.put(nextHash, entityId);
        repaint();
        return true;
    }

    private void printTextState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String id = cellOccupancy.get(i * cols + j);
                if (id == null) sb.append( (i == endR && j == endC) ? "E " : ". ");
                else sb.append(id.equals("Bunny") ? "B " : "R ");
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }

    public int[] getBunnyPosition() {
        for (Map.Entry<Integer, String> entry : cellOccupancy.entrySet()) {
            if (entry.getValue().equals("Bunny")) {
                int hash = entry.getKey();
                return new int[]{hash / cols, hash % cols};
            }
        }
        return null;
    }

    private int[] getRandomEmptyCell() {
        while (true) {
            int r = (int)(Math.random() * rows);
            int c = (int)(Math.random() * cols);
            if (!cellOccupancy.containsKey(r * cols + c)) {
                return new int[]{r, c};
            }
        }
    }

    public int[] getRandomValidMove(int r, int c) {
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        List<int[]> valid = new ArrayList<>();
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                if (!isWallBetween(r, c, nr, nc)) valid.add(new int[]{nr, nc});
            }
        }
        if (valid.isEmpty()) return null;
        return valid.get((int)(Math.random() * valid.size()));
    }

    public int[] getMoveTowards(int r, int c, int targetR, int targetC) {
        int bestDist = 9999;
        int[] bestMove = null;
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !isWallBetween(r, c, nr, nc)) {
                int dist = Math.abs(nr - targetR) + Math.abs(nc - targetC);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestMove = new int[]{nr, nc};
                }
            }
        }
        return bestMove;
    }

    public boolean isWallBetween(int r, int c, int nr, int nc) {
        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) return true;
        if (nr == r - 1) return hWalls[r][c];
        if (nr == r + 1) return hWalls[nr][c];
        if (nc == c - 1) return vWalls[r][c];
        if (nc == c + 1) return vWalls[r][nc];
        return true;
    }

    public void resetGrid() {
        gameRunning = false;
        cellOccupancy.clear();
        rows = (int) frame.configPanel.rowsSpinner.getValue();
        cols = (int) frame.configPanel.colsSpinner.getValue();

        hWalls = new boolean[rows + 1][cols];
        vWalls = new boolean[rows][cols + 1];
        for (int i = 0; i <= rows; i++) Arrays.fill(hWalls[i], true);
        for (int i = 0; i < rows; i++) Arrays.fill(vWalls[i], true);

        int[] start = getRandomEdgeCell();
        startR = start[0]; startC = start[1];
        int[] end;
        do { end = getRandomEdgeCell(); } while (end[0] == startR && end[1] == startC);
        endR = end[0]; endC = end[1];

        repaint();
    }

    private int[] getRandomEdgeCell() {
        int side = (int)(Math.random() * 4);
        if (side == 0) return new int[]{0, (int)(Math.random() * cols)};
        if (side == 1) return new int[]{rows - 1, (int)(Math.random() * cols)};
        if (side == 2) return new int[]{(int)(Math.random() * rows), 0};
        return new int[]{(int)(Math.random() * rows), cols - 1};
    }

    public void removeRandomWall() {
        if (hWalls == null) resetGrid();
        boolean isHoriz = Math.random() < 0.5;
        if (isHoriz) {
            int r = (int) (Math.random() * (rows - 1)) + 1;
            int c = (int) (Math.random() * cols);
            hWalls[r][c] = false;
        } else {
            int r = (int) (Math.random() * rows);
            int c = (int) (Math.random() * (cols - 1)) + 1;
            vWalls[r][c] = false;
        }
        repaint();
    }

    public void generateMazeAnimated(int delay) {
        resetGrid();
        visitedGen = new boolean[rows][cols];
        stack = new Stack<>();
        int[] start = {startR, startC};
        visitedGen[startR][startC] = true;
        stack.push(start);

        genTimer = new Timer(delay, e -> {
            if (!stack.isEmpty()) {
                stepGeneration();
                repaint();
            } else {
                ((Timer)e.getSource()).stop();
            }
        });
        genTimer.start();
    }

    private void stepGeneration() {
        int[] curr = stack.peek();
        int r = curr[0], c = curr[1];
        List<int[]> neighbors = new ArrayList<>();
        int[][] dirs = {{-1,0,0}, {1,0,1}, {0,-1,2}, {0,1,3}};

        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visitedGen[nr][nc]) {
                neighbors.add(new int[]{nr, nc, d[2]});
            }
        }

        if (!neighbors.isEmpty()) {
            int[] next = neighbors.get((int)(Math.random() * neighbors.size()));
            int nr = next[0], nc = next[1], dir = next[2];
            if (dir == 0) hWalls[r][c] = false;
            else if (dir == 1) hWalls[r+1][c] = false;
            else if (dir == 2) vWalls[r][c] = false;
            else if (dir == 3) vWalls[r][c+1] = false;
            visitedGen[nr][nc] = true;
            stack.push(new int[]{nr, nc});
        } else {
            stack.pop();
        }
    }

    public boolean checkTraversability() {
        if (hWalls == null) return false;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startR, startC});
        visited[startR][startC] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0], c = curr[1];
            if (r == endR && c == endC) return true;
            if (r > 0 && !hWalls[r][c] && !visited[r-1][c]) { visited[r-1][c] = true; queue.add(new int[]{r-1, c}); }
            if (r < rows - 1 && !hWalls[r+1][c] && !visited[r+1][c]) { visited[r+1][c] = true; queue.add(new int[]{r+1, c}); }
            if (c > 0 && !vWalls[r][c] && !visited[r][c-1]) { visited[r][c-1] = true; queue.add(new int[]{r, c-1}); }
            if (c < cols - 1 && !vWalls[r][c+1] && !visited[r][c+1]) { visited[r][c+1] = true; queue.add(new int[]{r, c+1}); }
        }
        return false;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hWalls == null) return;
        int cellW = canvasWidth / cols, cellH = canvasHeight / rows;

        g.setColor(Color.RED); g.fillRect(startC * cellW, startR * cellH, cellW, cellH);
        g.setColor(Color.GREEN); g.fillRect(endC * cellW, endR * cellH, cellW, cellH);

        for (Map.Entry<Integer, String> entry : cellOccupancy.entrySet()) {
            int hash = entry.getKey(), er = hash / cols, ec = hash % cols;
            if (entry.getValue().equals("Bunny")) {
                g.setColor(Color.PINK); g.fillOval(ec * cellW + 4, er * cellH + 4, cellW - 8, cellH - 8);
            } else {
                g.setColor(Color.BLUE); g.fillRect(ec * cellW + 4, er * cellH + 4, cellW - 8, cellH - 8);
            }
        }

        g.setColor(Color.WHITE);
        for (int r = 0; r <= rows; r++)
            for (int c = 0; c < cols; c++)
                if (hWalls[r][c]) g.fillRect(c * cellW, r * cellH - (r==rows?wallThick:0), cellW, wallThick);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c <= cols; c++)
                if (vWalls[r][c]) g.fillRect(c * cellW - (c==cols?wallThick:0), r * cellH, wallThick, cellH);
    }

    private void initMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (hWalls == null || gameRunning) return;
                int cellW = canvasWidth / cols, cellH = canvasHeight / rows;
                int x = e.getX(), y = e.getY();
                int c = Math.min(x / cellW, cols - 1), r = Math.min(y / cellH, rows - 1);
                int rx = x % cellW, ry = y % cellH;
                int min = Math.min(Math.min(ry, cellH - ry), Math.min(rx, cellW - rx));
                if (min < wallThick * 2) {
                    if (min == ry) hWalls[r][c] = !hWalls[r][c];
                    else if (min == cellH - ry) hWalls[r + 1][c] = !hWalls[r + 1][c];
                    else if (min == rx) vWalls[r][c] = !vWalls[r][c];
                    else vWalls[r][c + 1] = !vWalls[r][c + 1];
                    repaint();
                }
            }
        });

    }

    public int getRows(){
        return this.rows;
    }

    public int getCols(){
        return this.cols;
    }

    public int getEndR(){
        return this.endR;
    }

    public int getEndC(){
        return this.endC;
    }
}