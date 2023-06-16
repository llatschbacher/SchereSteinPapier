import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Klasse die bei einer TCP Kommunikation den Server und seine Funktionen darstellt
 *
 * @author llatschbacher
 * @version 2023-06-16
 */

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
            // Server-Socket erstellen und auf Verbindungen warten
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server gestartet. Warte auf Verbindungen...");

            // Spieler 1 akzeptieren
            Player player1 = waitAndAcceptPlayer(serverSocket, 1);

            // Spieler 2 akzeptieren
            Player player2 = waitAndAcceptPlayer(serverSocket, 2);

            // Spiel initialisieren
            game = new Game(player1, player2, new Stats());

            // ClientHandler-Threads für Spieler 1 und 2 starten
            executorService.execute(new ClientHandler(player1, player2));
            executorService.execute(new ClientHandler(player2, player1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Player waitAndAcceptPlayer(ServerSocket serverSocket, int playerNumber) throws IOException {
        // Auf eine Verbindung von einem Spieler warten
        Socket socket = serverSocket.accept();
        Player player = new Player(socket);
        System.out.println("Spieler " + playerNumber + " verbunden.");
        return player;
    }

    private void handleClient(Player currentPlayer, Player otherPlayer) throws IOException {
        // Den Namen des aktuellen Spielers empfangen und setzen
        String playerName = currentPlayer.receive();
        currentPlayer.setName(playerName);
        currentPlayer.flush();

        // Warten, bis der andere Spieler einen Namen hat
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

        // Das Spiel starten
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
            // Die handleClient-Methode im neuen Thread aufrufen
            try {
                handleClient(player1, player2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
