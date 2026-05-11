package ServerApplication;

import com.example.entity.Player;
import com.example.entity.Question;
import com.example.entity.GameResult;
import com.example.service.GameService;
import com.example.config.JpaConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * ClientThread handles communication with a single game client.
 * Game results are persisted to the database using JPA.
 */
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

            // Read player name
            String playerName = in.readLine();
            gameSession.addPlayer(playerId, playerName);
            out.println("Welcome " + playerName + "! Game starting...");

            // Set timeout for reading answers (per question)
            int timeoutMs = gameSession.getTimePerQuestion() * 1000;
            socket.setSoTimeout(timeoutMs);

            // Game loop - iterate through questions
            while (gameSession.hasMoreQuestions(playerId)) {
                Question question = gameSession.getCurrentQuestion(playerId);
                out.println("QUESTION:" + gameSession.getTotalQuestions() + "|" + question.getFormattedQuestion());
                out.println("TIME:" + gameSession.getTimePerQuestion());

                String line = null;
                boolean timedOut = false;

                try {
                    line = in.readLine();
                } catch (SocketTimeoutException e) {
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

                // If timed out, mark as wrong answer
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
                        answer = null;
                        timedOut = true;
                    }
                }

                if (answer == null || "exit".equalsIgnoreCase(answer)) {
                    break;
                }

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

            // Mark player as finished
            gameSession.playerFinished();

            // Get the JPA Player entity for result persistence
            Player jpaPlayer = gameSession.getPlayer(playerId);

            // Send waiting signal
            out.println("WAITING_FOR_OTHERS");

            // Wait for all players to finish
            gameSession.waitForAllPlayersToFinish();

            // Save game result to database
            if (jpaPlayer != null) {
                gameSession.saveGameResult(
                    jpaPlayer,
                    jpaPlayer.getCorrectAnswers(),
                    jpaPlayer.getTotalResponseTime()
                );
            }

            // Send final results
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