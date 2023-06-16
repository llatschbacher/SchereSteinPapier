import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final ExecutorService executorService;
    private Game game;

    public Server(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server gestartet. Warte auf Verbindungen...");

            Player player1 = waitAndAcceptPlayer(serverSocket, 1);
            Player player2 = waitAndAcceptPlayer(serverSocket, 2);

            game = new Game(player1, player2, new Stats());
            new Thread(new ClientHandler(player1, player2)).start();
            new Thread(new ClientHandler(player2, player1)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Player waitAndAcceptPlayer(ServerSocket serverSocket, int playerNumber) throws IOException {
        Socket socket = serverSocket.accept();
        Player player = new Player(socket);
        System.out.println("Spieler " + playerNumber + " verbunden.");
        return player;
    }

    private void handleClient(Player currentPlayer, Player otherPlayer) throws IOException {
        String playerName = currentPlayer.receive();
        currentPlayer.setName(playerName);
        currentPlayer.flush();
        while (otherPlayer.getName() == null) {
            try {
                Thread.sleep(100); // Kurze Pause, um die CPU zu entlasten
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Wenn der andere Spieler einen Namen hat, sende seinen Namen an den aktuellen Spieler
        if (otherPlayer.getName() != null) {
            currentPlayer.send("Du bist mit " + otherPlayer.getName() + " verbunden!");
        }
        game.start();

        try {
            currentPlayer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Bitte geben Sie den Port als Argument an.");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Server server = new Server(port);
        server.start();
    }

    private class ClientHandler implements Runnable {
        private final Player player1;
        private final Player player2;

        public ClientHandler(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        @Override
        public void run() {
            // Existierende statische handleClient-Methode vom neuen Thread aus aufrufen
            try {
                handleClient(player1, player2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
