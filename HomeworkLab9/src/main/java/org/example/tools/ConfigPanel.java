package org.example.tools;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    public JSpinner rowsSpinner;
    public JSpinner colsSpinner;
    JButton drawBtn;
    JButton generateBtn;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        label = new JLabel("Maze Size:");
        rowsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        colsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        drawBtn = new JButton("Draw Maze");
        generateBtn = new JButton("Generate Maze");

        drawBtn.addActionListener(e -> frame.canvas.resetGrid());
        generateBtn.addActionListener(e -> frame.canvas.generateMazeAnimated(10));

        add(label);
        add(rowsSpinner);
        add(colsSpinner);
        add(drawBtn);
        add(generateBtn);
    }
}