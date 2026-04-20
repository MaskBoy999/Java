package org.example.tools;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton createBtn = new JButton("Create");
    JButton resetBtn = new JButton("Reset");
    JButton exitBtn = new JButton("Exit");

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        add(createBtn);
        add(resetBtn);
        add(exitBtn);

        exitBtn.addActionListener(e -> frame.dispose());
        resetBtn.addActionListener(e -> frame.canvas.resetGrid());
        createBtn.addActionListener(e -> frame.canvas.removeRandomWall());
    }
}