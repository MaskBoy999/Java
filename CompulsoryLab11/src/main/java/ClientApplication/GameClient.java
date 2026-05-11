package ClientApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8100;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            AtomicBoolean running = new AtomicBoolean(true);
            final long[] questionReceivedTime = {0};

            //server
            Thread serverListener = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                        if (response.startsWith("QUESTION:")) {
                            questionReceivedTime[0] = System.currentTimeMillis();
                        }
                        // Când primește GAME_OVER, citește și restul (leaderboard + winner)
                        if (response.equals("Server stopped") || response.startsWith("GAME_OVER")) {
                            // Citește leaderboard
                            String line;
                            while ((line = in.readLine()) != null && !line.startsWith("WINNER:")) {
                                System.out.println(line);
                            }
                            // Citește winner
                            if (line != null) {
                                System.out.println(line);
                            }
                            running.set(false);
                            break;
                        }
                    }
                } catch (IOException e) {
                    if (running.get()) {
                        System.err.println("Lost connection to server");
                    }
                }
            });
            serverListener.start();

            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();
            out.println(playerName);

            // tastatura
            Thread keyboardInput = new Thread(() -> {
                try {
                    String input;
                    while (running.get() && scanner.hasNextLine()) {
                        input = scanner.nextLine();
                        if ("exit".equalsIgnoreCase(input)) {
                            out.println("exit");
                            break;
                        }

                        long responseTime = System.currentTimeMillis() - questionReceivedTime[0];
                        out.println("TIME:" + responseTime);
                        out.println(input);
                    }
                } catch (Exception e) {
                }
            });
            keyboardInput.start();

            keyboardInput.join();
            serverListener.join();
            socket.close();

        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e);
        } catch (IOException e) {
            System.err.println("I/O error: " + e);
        } catch (InterruptedException e) {
            System.err.println("Client interrupted");
        }
    }
}