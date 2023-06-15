import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (
                ServerSocket server = new ServerSocket(5000);
        ) {
            while (true) {
                Socket client = server.accept();
                ClientHandler handler = new ClientHandler(client);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void handleClient(Socket client) {
        try (
                client;
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ) {
            // ... Kommunikation mit dem Client ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ClientHandler implements Runnable {
        private final Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            handleClient(client);
        }
    }
}
