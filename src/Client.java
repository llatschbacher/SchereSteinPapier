import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public void connectToServer() {
        try (
                ServerSocket server = new ServerSocket(5000);
        ) {
            while (true) {
                Socket client = server.accept();

                // Anstatt (auf dem main-Thread) den Client zu bearbeiten
                // wird hier jetzt ein neuer Thread gestartet.
                ClientHandler handler = new ClientHandler(client);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to server.");

            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            Scanner scanner = new Scanner(System.in);
            String command;
            while (true) {
                System.out.print("Enter command (get <instrument> | exit): ");
                command = scanner.nextLine();

                outputStream.writeObject(command);
                outputStream.flush();

                if (command.equals("exit")) {
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    private static class ClientHandler implements Runnable {
        private final Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // Existierende statische handleClient-Methode vom neuen Thread aus aufrufen
            handleClient(client);
        }
    }

    private static void handleClient(Socket client) {
        try (
                client;
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        ){

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connectToServer();
    }
}

