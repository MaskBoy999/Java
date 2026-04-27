package org.example.tools;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton startBtn = new JButton("Start Game");
    JButton stopBtn = new JButton("Force Stop");
    JButton createBtn = new JButton("Create");
    JButton resetBtn = new JButton("Reset");
    JButton validateBtn = new JButton("Validate");

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        add(startBtn);
        add(stopBtn);
        add(createBtn);
        add(resetBtn);
        add(validateBtn);

        startBtn.addActionListener(e -> frame.canvas.startGame());
        stopBtn.addActionListener(e -> frame.canvas.stopGame("Game forced to stop."));
        resetBtn.addActionListener(e -> frame.canvas.resetGrid());
        createBtn.addActionListener(e -> frame.canvas.removeRandomWall());

        validateBtn.addActionListener(e -> {
            boolean reachable = frame.canvas.checkTraversability();
            JOptionPane.showMessageDialog(frame, reachable ? "Path exists." : "No path found.");
        });
    }
}