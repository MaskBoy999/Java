// Inlocuiti clasa InputController cu aceasta versiune mai stabila
package org.example.simulation;

import java.util.Scanner;
import java.util.List;
import org.example.tools.DrawingPanel;

public class InputController extends Thread {
    private final List<GameParticipant> participants;
    private final DrawingPanel canvas;

    public InputController(List<GameParticipant> participants, DrawingPanel canvas) {
        this.participants = participants;
        this.canvas = canvas;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Sistem de control activ. Astept comenzi...");

        while (canvas.isGameRunning()) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim().toLowerCase();

                if (line.isEmpty()) continue;

                if (line.equals("stop")) {
                    System.out.println(">>> Comanda primita: STOP");
                    for (GameParticipant p : participants) p.setPaused(true);
                } else if (line.equals("resume")) {
                    System.out.println(">>> Comanda primita: RESUME");
                    for (GameParticipant p : participants) p.resumeParticipant();
                } else if (line.equals("slow")) {
                    System.out.println(">>> Viteza redusa");
                    for (GameParticipant p : participants) p.setDelay(1000);
                } else if (line.equals("fast")) {
                    System.out.println(">>> Viteza maxima");
                    for (GameParticipant p : participants) p.setDelay(100);
                }
            }
        }
    }
}