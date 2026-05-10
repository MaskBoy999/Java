package ClientApplication;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class RunClients {
    public static void main(String[] args) throws Exception {
        int numClients = 11;
        List<Process> processes = new ArrayList<>();
        List<OutputStream> outputs = new ArrayList<>();

        System.out.println("Starting " + numClients + " GameClient instances...");

        String javaExe = System.getProperty("java.home") + "\\bin\\java.exe";
        String classpath = "target/classes";

        for (int i = 0; i < numClients; i++) {
            ProcessBuilder pb = new ProcessBuilder(
                javaExe,
                "-cp", classpath,
                "ClientApplication.GameClient"
            );
            pb.redirectErrorStream(true);
            Process p = pb.start();
            processes.add(p);
            outputs.add(p.getOutputStream());

            System.out.println("Started client " + (i + 1));

            // Așteaptă puțin între conectări pentru a testa perioada de 10 secunde
            if (i < numClients - 1) {
                Thread.sleep(200); // 200ms pauză între clienți
            }
        }

        // Trimite numele pentru fiecare client
        Thread.sleep(2000); // Așteaptă puțin să se conecteze

        for (int i = 0; i < numClients; i++) {
            String playerName = "Player" + (i + 1);
            outputs.get(i).write((playerName + "\n").getBytes());
            outputs.get(i).flush();
            System.out.println("Sent name: " + playerName);
        }

        System.out.println("All " + numClients + " clients tried to connect with names!");
        System.out.println("Press Enter to stop all clients...");

        System.in.read();

        for (Process p : processes) {
            p.destroy();
        }

        System.out.println("All clients stopped.");
    }
}