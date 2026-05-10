package ServerApplication;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameSession {
    private final List<Question> questions;
    private final int timePerQuestion;
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    // Fiecare jucător are propriul index de întrebare
    private final Map<String, Integer> playerQuestionIndex = new ConcurrentHashMap<>();
    // Numărul total de jucători
    private final AtomicInteger totalPlayers = new AtomicInteger(0);
    // Numărul de jucători care au terminat
    private final AtomicInteger finishedPlayers = new AtomicInteger(0);

    public GameSession(List<Question> questions, int timePerQuestion) {
        this.questions = questions;
        this.timePerQuestion = timePerQuestion;
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

    // Verifică dacă un jucător nou se poate alătura
    public boolean canJoin() {
        return joinWindowOpen;
    }

    // Pornește fereastra de 10 secunde pentru alți jucători
    private void startJoinWindow() {
        if (joinWindowOpen) {
            synchronized (joinLock) {
                if (joinWindowOpen) {
                    System.out.println("First player joined. Starting 10 second join window...");

                    // Thread separat care după 10 secunde închide fereastra
                    new Thread(() -> {
                        try {
                            Thread.sleep(10000); // 10 secunde
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        joinWindowOpen = false; // Închide fereastra DUPĂ 10 secunde
                        System.out.println("Join period ended. Total players: " + totalPlayers.get());
                    }).start();
                }
            }
        }
    }

    // Când un jucător termină
    public void playerFinished() {
        finishedPlayers.incrementAndGet();
    }

    // Așteaptă până când toți jucătorii au terminat
    public void waitForAllPlayersToFinish() {
        int expectedTotal = totalPlayers.get();

        // Dacă e doar 1 jucător, nu așteaptă
        if (expectedTotal <= 1) {
            return;
        }

        System.out.println("Waiting for all " + expectedTotal + " players to finish...");

        // Așteaptă până termină toți sau timeout 30 secunde
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

    public void addPlayer(String playerId, String playerName) {
        // Incrementează mai întâi pentru a evita race condition
        int currentCount = incrementAndGetTotal();

        // Dacă e primul jucător (count == 1), pornește fereastra de 10 secunde
        if (currentCount == 1) {
            startJoinWindow();
        }

        players.put(playerId, new Player(playerName));
        playerQuestionIndex.put(playerId, 0); // Începe de la întrebarea 0
    }

    // Metodă helper care returnează valoarea nouă după incrementare
    public int incrementAndGetTotal() {
        return totalPlayers.incrementAndGet();
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public void recordAnswer(String playerId, boolean correct, long responseTime) {
        Player player = players.get(playerId);
        if (player != null) {
            player.recordAnswer(correct, responseTime);
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

    public static class Player {
        private final String name;
        private int correctAnswers;
        private long totalResponseTime;

        public Player(String name) {
            this.name = name;
            this.correctAnswers = 0;
            this.totalResponseTime = 0;
        }

        public String getName() {
            return name;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }

        public long getTotalResponseTime() {
            return totalResponseTime;
        }

        public void recordAnswer(boolean correct, long responseTime) {
            if (correct) {
                correctAnswers++;
            }
            totalResponseTime += responseTime;
        }

        public boolean isBetterThan(Player other) {
            if (this.correctAnswers != other.correctAnswers) {
                return this.correctAnswers > other.correctAnswers;
            }
            return this.totalResponseTime < other.totalResponseTime;
        }
    }
}