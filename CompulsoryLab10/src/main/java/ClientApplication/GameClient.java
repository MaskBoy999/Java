package ClientApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClient {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 8100;
        try {
            Socket socket = new Socket(serverAddress, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            String request;
            System.out.println("Conectat la server. Introduceti comenzi:");
            while (true) {
                request = scanner.nextLine();
                if (request.equals("exit")) {
                    break;
                }
                out.println(request);
                String response = in.readLine();
                if (response != null) {
                    System.out.println(response);
                    if (response.equals("Server stopped")) {
                        break;
                    }
                } else {
                    break;
                }
            }
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Serverul nu a putut fi gasit: " + e);
        } catch (IOException e) {
            System.err.println("Eroare de I/O: " + e);
        }
    }
}