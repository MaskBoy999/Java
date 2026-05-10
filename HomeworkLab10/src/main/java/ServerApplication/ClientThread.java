package ServerApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientThread extends Thread {
    private final Socket socket;
    private final GameServer server;
    private final GameSession gameSession;
    private final String playerId;
    private static int playerCounter = 0;

    public ClientThread(Socket socket, GameServer server, GameSession gameSession) {
        this.socket = socket;
        this.server = server;
        this.gameSession = gameSession;
        this.playerId = "player-" + (++playerCounter);
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String playerName = in.readLine();
            gameSession.addPlayer(playerId, playerName);
            out.println("Welcome " + playerName + "! Game starting...");

            // Setează timeout pentru citire DOAR în timpul jocului (timpul per întrebare * 1000 ms)
            int timeoutMs = gameSession.getTimePerQuestion() * 1000;
            socket.setSoTimeout(timeoutMs);

            while (gameSession.hasMoreQuestions(playerId)) {
                Question question = gameSession.getCurrentQuestion(playerId);
                out.println("QUESTION:" + gameSession.getTotalQuestions() + "|" + question.getFormattedQuestion());
                out.println("TIME:" + gameSession.getTimePerQuestion());

                String line = null;
                boolean timedOut = false;

                try {
                    line = in.readLine();
                } catch (SocketTimeoutException e) {
                    // Timeout - jucătorul nu a răspuns la timp
                    timedOut = true;
                }

                if (line == null || "exit".equalsIgnoreCase(line)) {
                    break;
                }

                if ("stop".equalsIgnoreCase(line)) {
                    out.println("Server stopped");
                    server.stopServer();
                    break;
                }

                // Dacă a expirat timpul, considerăm răspunsul greșit
                if (timedOut) {
                    out.println("TIME'S UP! Correct answer: " + question.getCorrectAnswer());
                    gameSession.recordAnswer(playerId, false, gameSession.getTimePerQuestion() * 1000);
                    gameSession.nextQuestion(playerId);
                    continue;
                }

                long responseTime = 0;
                String answer = line;

                if (line.startsWith("TIME:")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        responseTime = Long.parseLong(parts[1]);
                    }
                    try {
                        answer = in.readLine();
                    } catch (SocketTimeoutException e) {
                        // Timeout pe al doilea read (răspunsul propriu-zis)
                        answer = null;
                        timedOut = true;
                    }
                }

                if (answer == null || "exit".equalsIgnoreCase(answer)) {
                    break;
                }

                // Dacă a expirat timpul între TIME și răspuns
                if (timedOut) {
                    out.println("TIME'S UP! Correct answer: " + question.getCorrectAnswer());
                    gameSession.recordAnswer(playerId, false, responseTime);
                    gameSession.nextQuestion(playerId);
                    continue;
                }

                boolean correct = question.isCorrect(answer);
                gameSession.recordAnswer(playerId, correct, responseTime);

                if (correct) {
                    out.println("CORRECT! Response time: " + responseTime + "ms");
                } else {
                    out.println("WRONG! Correct answer: " + question.getCorrectAnswer());
                }

                gameSession.nextQuestion(playerId);
            }

            // Marcăm că acest jucător a terminat
            gameSession.playerFinished();

            // Trimitem "WAITING..." ca să știe clientul să aștepte
            out.println("WAITING_FOR_OTHERS");

            // Așteptăm ca toți jucătorii să termine înainte să afișăm rezultatul
            gameSession.waitForAllPlayersToFinish();

            // Acum toți au terminat, putem afișa rezultatul
            out.println("GAME_OVER");
            out.println(gameSession.getLeaderboard());
            out.println("WINNER:" + gameSession.getWinner());

        } catch (IOException e) {
            System.err.println("Communication error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}