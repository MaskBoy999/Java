package org.example.tools;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    JSpinner rowsSpinner;
    JSpinner colsSpinner;
    JButton drawBtn;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        label = new JLabel("Maze Size:");
        rowsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        colsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        drawBtn = new JButton("Draw Maze");

        drawBtn.addActionListener(e -> frame.canvas.drawGrid());

        add(label);
        add(rowsSpinner);
        add(colsSpinner);
        add(drawBtn);
    }
}