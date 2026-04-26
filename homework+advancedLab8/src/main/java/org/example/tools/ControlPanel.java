package org.example.tools;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton createBtn = new JButton("Create");
    JButton resetBtn = new JButton("Reset");
    JButton validateBtn = new JButton("Validate");
    JButton saveBtn = new JButton("Save");
    JButton loadBtn = new JButton("Load");
    JButton exportBtn = new JButton("Export PNG");
    JButton exitBtn = new JButton("Exit");

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        add(createBtn);
        add(resetBtn);
        add(validateBtn);
        add(saveBtn);
        add(loadBtn);
        add(exportBtn);
        add(exitBtn);

        exitBtn.addActionListener(e -> frame.dispose());
        resetBtn.addActionListener(e -> frame.canvas.resetGrid());
        createBtn.addActionListener(e -> frame.canvas.removeRandomWall());

        validateBtn.addActionListener(e -> {
            boolean reachable = frame.canvas.checkTraversability();
            JOptionPane.showMessageDialog(frame, reachable ? "Path exists." : "No path found.");
        });

        saveBtn.addActionListener(e -> frame.canvas.saveMaze("maze.dat"));
        loadBtn.addActionListener(e -> frame.canvas.loadMaze("maze.dat"));
        exportBtn.addActionListener(e -> frame.canvas.exportToPNG("maze_capture.png"));
    }
}