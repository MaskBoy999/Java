package ServerApplication;

import com.example.entity.Player;
import com.example.entity.Question;
import com.example.entity.GameResult;
import com.example.repository.GameResultRepository;
import com.example.repository.PlayerRepository;
import com.example.service.GameService;
import com.example.config.JpaConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages a game session. Uses JPA (via Spring Data JPA) to persist
 * players and game results to the database.
 */
public class GameSession {
    private final List<Question> questions;
    private final int timePerQuestion;

    // Maps playerId -> JPA Player entity
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    // Each player has their own question index
    private final Map<String, Integer> playerQuestionIndex = new ConcurrentHashMap<>();

    private final AtomicInteger totalPlayers = new AtomicInteger(0);
    private final AtomicInteger finishedPlayers = new AtomicInteger(0);

    // JPA services
    private final GameService gameService;
    private final PlayerRepository playerRepository;
    private final GameResultRepository gameResultRepository;

    public GameSession(List<Question> questions, int timePerQuestion) {
        this.questions = questions;
        this.timePerQuestion = timePerQuestion;

        // Initialize Spring context for JPA
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);
        this.gameService = context.getBean(GameService.class);
        this.playerRepository = context.getBean(PlayerRepository.class);
        this.gameResultRepository = context.getBean(GameResultRepository.class);
    }

    public int getTotalPlayerCount() {
        return totalPlayers.get();
    }

    public void incrementTotalPlayers() {
        totalPlayers.incrementAndGet();
    }

    // Flag pentru perioada de join (10 secunde)
    private volatile boolean joinWindowOpen = true;
    private final Object joinLock = new Object();

    public boolean canJoin() {
        return joinWindowOpen;
    }

    private void startJoinWindow() {
        if (joinWindowOpen) {
            synchronized (joinLock) {
                if (joinWindowOpen) {
                    System.out.println("First player joined. Starting 10 second join window...");

                    new Thread(() -> {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        joinWindowOpen = false;
                        System.out.println("Join period ended. Total players: " + totalPlayers.get());
                    }).start();
                }
            }
        }
    }

    public void playerFinished() {
        finishedPlayers.incrementAndGet();
    }

    public void waitForAllPlayersToFinish() {
        int expectedTotal = totalPlayers.get();

        if (expectedTotal <= 1) {
            return;
        }

        System.out.println("Waiting for all " + expectedTotal + " players to finish...");

        long startTime = System.currentTimeMillis();
        long maxWait = 30000;

        while (finishedPlayers.get() < expectedTotal) {
            if (System.currentTimeMillis() - startTime > maxWait) {
                System.out.println("Timeout reached");
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("All " + finishedPlayers.get() + " players finished!");
    }

    public Question getCurrentQuestion(String playerId) {
        int index = playerQuestionIndex.getOrDefault(playerId, 0);
        if (index >= questions.size()) {
            return null;
        }
        return questions.get(index);
    }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public boolean hasMoreQuestions(String playerId) {
        int index = playerQuestionIndex.getOrDefault(playerId, 0);
        return index < questions.size();
    }

    public void nextQuestion(String playerId) {
        playerQuestionIndex.compute(playerId, (key, val) -> val + 1);
    }

    /**
     * Adds a player to the session and persists them via JPA.
     */
    public void addPlayer(String playerId, String playerName) {
        int currentCount = incrementAndGetTotal();

        if (currentCount == 1) {
            startJoinWindow();
        }

        // Persist player via JPA
        Player jpaPlayer = new Player(playerName);
        jpaPlayer = gameService.savePlayer(jpaPlayer);

        players.put(playerId, jpaPlayer);
        playerQuestionIndex.put(playerId, 0);
    }

    public int incrementAndGetTotal() {
        return totalPlayers.incrementAndGet();
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    /**
     * Records an answer for a player and updates them in the database.
     */
    public void recordAnswer(String playerId, boolean correct, long responseTime) {
        Player player = players.get(playerId);
        if (player != null) {
            player.recordAnswer(correct, responseTime);
            try {
                gameService.savePlayer(player);
            } catch (Exception e) {
                System.err.println("Warning: Could not update player in DB: " + e.getMessage());
            }
        }
    }

    /**
     * Saves a game result to the database.
     */
    public void saveGameResult(Player player, int correctAnswers, long totalResponseTime) {
        try {
            GameResult result = new GameResult(player, correctAnswers, totalResponseTime);
            gameService.saveGameResult(result);
        } catch (Exception e) {
            System.err.println("Warning: Could not save game result: " + e.getMessage());
        }
    }

    public String getWinner() {
        Player winner = null;
        for (Player player : players.values()) {
            if (winner == null || player.isBetterThan(winner)) {
                winner = player;
            }
        }
        return winner != null ? winner.getName() : "No winner";
    }

    public String getLeaderboard() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LEADERBOARD ===\n");
        for (Player player : players.values()) {
            sb.append(player.getName())
              .append(" - Correct: ")
              .append(player.getCorrectAnswers())
              .append(", Time: ")
              .append(player.getTotalResponseTime())
              .append("ms\n");
        }
        return sb.toString();
    }

    public int getTotalQuestions() {
        return questions.size();
    }
}