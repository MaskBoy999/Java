package org.example.tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.swing.Timer;

public class DrawingPanel extends JPanel implements Serializable {
    private final MainFrame frame;
    private int rows, cols;
    private final int canvasWidth = 600;
    private final int canvasHeight = 600;
    private boolean[][] hWalls, vWalls;
    private int startR, startC, endR, endC;
    private final int wallThick = 6;

    private Timer genTimer;
    private Stack<int[]> stack;
    private boolean[][] visitedGen;

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        setBackground(Color.BLACK);
        initMouseListener();
    }

    public void resetGrid() {
        if (genTimer != null) genTimer.stop();
        rows = (int) frame.configPanel.rowsSpinner.getValue();
        cols = (int) frame.configPanel.colsSpinner.getValue();

        hWalls = new boolean[rows + 1][cols];
        vWalls = new boolean[rows][cols + 1];
        for (int i = 0; i <= rows; i++) Arrays.fill(hWalls[i], true);
        for (int i = 0; i < rows; i++) Arrays.fill(vWalls[i], true);

        int[] start = getRandomEdgeCell();
        startR = start[0];
        startC = start[1];
        int[] end;
        do{
            end = getRandomEdgeCell();
        }while (end[0] == startR && end[1] == startC);
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
        int r = curr[0];
        int c = curr[1];

        java.util.List<int[]> neighbors = new ArrayList<>();
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

            if (r > 0 && !hWalls[r][c] && !visited[r-1][c]) {
                visited[r-1][c] = true; queue.add(new int[]{r-1, c});
            }
            if (r < rows - 1 && !hWalls[r+1][c] && !visited[r+1][c]) {
                visited[r+1][c] = true; queue.add(new int[]{r+1, c});
            }
            if (c > 0 && !vWalls[r][c] && !visited[r][c-1]) {
                visited[r][c-1] = true; queue.add(new int[]{r, c-1});
            }
            if (c < cols - 1 && !vWalls[r][c+1] && !visited[r][c+1]) {
                visited[r][c+1] = true; queue.add(new int[]{r, c+1});
            }
        }
        return false;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hWalls == null) return;
        int cellW = canvasWidth / cols;
        int cellH = canvasHeight / rows;

        g.setColor(Color.RED);
        g.fillRect(startC * cellW, startR * cellH, cellW, cellH);
        g.setColor(Color.GREEN);
        g.fillRect(endC * cellW, endR * cellH, cellW, cellH);

        g.setColor(Color.WHITE);
        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (hWalls[r][c]) g.fillRect(c * cellW, r * cellH - (r==rows?wallThick:0), cellW, wallThick);
            }
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols; c++) {
                if (vWalls[r][c]) g.fillRect(c * cellW - (c==cols?wallThick:0), r * cellH, wallThick, cellH);
            }
        }
    }

    private void initMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (hWalls == null) return;
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

    public void saveMaze(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(hWalls);
            oos.writeObject(vWalls);
            oos.writeInt(rows);
            oos.writeInt(cols);
            oos.writeInt(startR);
            oos.writeInt(startC);
            oos.writeInt(endR);
            oos.writeInt(endC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMaze(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            hWalls = (boolean[][]) ois.readObject();
            vWalls = (boolean[][]) ois.readObject();
            rows = ois.readInt();
            cols = ois.readInt();
            startR = ois.readInt();
            startC = ois.readInt();
            endR = ois.readInt();
            endC = ois.readInt();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exportToPNG(String path) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        this.paint(g2d);
        g2d.dispose();
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}