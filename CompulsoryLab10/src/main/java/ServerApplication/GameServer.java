package ServerApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class GameServer {
    public static final int PORT = 8100;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public GameServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Serverul a pornit.");
            while (running) {
                Socket socket = serverSocket.accept();
                new ClientThread(socket, this).start();
            }
        } catch (SocketException e) {
            if (running) System.err.println("Eroare la socket: " + e);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new GameServer();
    }
}