package ServerApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer {
    public static final int PORT = 8100;
    private static final int TIME_PER_QUESTION = 30;

    private volatile boolean running = true;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private GameSession gameSession;

    public GameServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            threadPool = Executors.newFixedThreadPool(10);

            List<Question> questions = QuestionLoader.loadQuestions("questions.txt");
            gameSession = new GameSession(questions, TIME_PER_QUESTION);

            System.out.println("Server started on port " + PORT);
            System.out.println("Loaded " + questions.size() + " questions");

            while (running) {
                Socket socket = serverSocket.accept();

                // Verifică dacă mai pot intra jucători
                if (!gameSession.canJoin()) {
                    System.out.println("New player rejected - join period ended");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // ignore
                    }
                    continue;
                }

                threadPool.execute(new ClientThread(socket, this, gameSession));
            }
        } catch (SocketException e) {
            if (running) System.err.println("Socket error: " + e);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            shutdown();
        }
    }

    public void stopServer() {
        running = false;
        shutdown();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void shutdown() {
        System.out.println("Shutting down...");
        if (threadPool != null) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
        }
        System.out.println("Server stopped");
    }

    public static void main(String[] args) {
        new GameServer();
    }
}