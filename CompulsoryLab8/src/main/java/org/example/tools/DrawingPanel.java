package org.example.tools;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    private final MainFrame frame;
    private int rows=10, cols=10;
    private final int canvasWidth = 600;
    private final int canvasHeight = 600;
    private boolean[][] isWall;

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEtchedBorder());
    }

    public void resetGrid() {
        isWall = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                isWall[i][j] = true;
            }
        }
        repaint();
    }

    public void drawGrid() {
        rows = (int) frame.configPanel.rowsSpinner.getValue();
        cols = (int) frame.configPanel.colsSpinner.getValue();
        isWall = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                isWall[i][j] = true;
            }
        }
        repaint();
    }

    public void removeRandomWall() {
        if (isWall == null) resetGrid();
        int r = (int) (Math.random() * rows);
        int c = (int) (Math.random() * cols);
        isWall[r][c] = false;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isWall == null) resetGrid();

        int cellW = canvasWidth / cols;
        int cellH = canvasHeight / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * cellW;
                int y = i * cellH;

                g.setColor(isWall[i][j] ? Color.WHITE : Color.BLACK);
                g.fillRect(x, y, cellW, cellH);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellW, cellH);
            }
        }
    }
}