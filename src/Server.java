import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Map<String, ClientHandler> clients;
    private CountDownLatch gameStartLatch;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(2); // Zwei Clients pro Spiel
            clients = new HashMap<>();
            gameStartLatch = new CountDownLatch(2); // Warten auf zwei Clients
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Server gestartet. Warte auf Verbindungen...");

        // Erstelle einen separaten Thread für den Server
        Thread serverThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Neue Verbindung akzeptiert.");

                    // Starte einen neuen Client-Handler-Thread
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    executorService.execute(clientHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        serverThread.start(); // Starte den Server-Thread

        // Warte auf Benutzereingabe im Haupt-Thread
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = consoleReader.readLine();

                // Verarbeite die Benutzereingabe oder beende den Server
                if (input.equals("exit")) {
                    break;
                } else {
                    // Hier kannst du weitere Aktionen basierend auf der Benutzereingabe durchführen
                }
            }

            // Beende den Server
            serverThread.interrupt();
            serverSocket.close();
            executorService.shutdown();
            consoleReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addClient(String playerName, ClientHandler clientHandler) {
        clients.put(playerName, clientHandler);
        if (clients.size() == 2) {
            startGame();
        }
    }

    private synchronized void removeClient(String playerName) {
        clients.remove(playerName);
    }

    private synchronized void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clients.values()) {
            clientHandler.sendMessage(message);
        }
    }

    private void startGame() {
        System.out.println("Zwei Spieler verbunden. Das Spiel beginnt!");
        broadcastMessage("Das Spiel beginnt!");

        // Spieler initialisieren
        Player player1 = new Player();
        Player player2 = new Player();

        // Namen der Spieler abfragen
        player1.warteAufNamen();
        player2.warteAufNamen();

        String name1 = player1.getName();
        String name2 = player2.getName();

        // Statistiken initialisieren
        Stats stats = new Stats();

        // Spiel initialisieren
        Game game = new Game(player1, player2, stats);

        while (true) {
            // Nachricht an beide Clients senden und Spielzug abfragen
            broadcastMessage("Mach deinen Spielzug (0 für Schere, 1 für Stein, 2 für Papier):");

            player1.warteAufSpielzug();
            player2.warteAufSpielzug();

            int spielzug1 = player1.getSpielzug();
            int spielzug2 = player2.getSpielzug();

            // Spielzug auswerten
            String gewinner = game.ermittelnUndAnzeigenDesGewinners();

            // Gewinner bekannt geben
            broadcastMessage("Gewinner: " + gewinner);

            // Spielstatistiken aktualisieren
            stats.aktualisiereStats(player1, player2, gewinner);

            // Weitere Spielrunde oder Spiel beenden
            broadcastMessage("Möchtest du weiterspielen? (ja/nein)");

            player1.warteAufAntwort();
            String antwort = player1.getAntwort();

            if (!antwort.equalsIgnoreCase("ja")) {
                break;
            }
        }

        // Spiel beendet
        broadcastMessage("Das Spiel ist beendet.");

        // Statistiken anzeigen
        broadcastMessage("Spieler " + name1 + ": Gewonnene Spiele - " + stats.getGewonneneSpiele(player1) + ", Verlorene Spiele - " + stats.getVerloreneSpiele(player1));
        broadcastMessage("Spieler " + name2 + ": Gewonnene Spiele - " + stats.getGewonneneSpiele(player2) + ", Verlorene Spiele - " + stats.getVerloreneSpiele(player2));

        // Weitere Aufräumarbeiten
        // ...
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Bitte geben Sie den Server-Port als Argument an.");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Server server = new Server(port);
        server.start();
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String playerName;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                // Namen vom Client empfangen
                playerName = reader.readLine();
                System.out.println("Spieler " + playerName + " hat sich verbunden.");

                // Client zum Server hinzufügen
                addClient(playerName, this);

                // Warten auf den Start des Spiels
                gameStartLatch.countDown(); // Verringert den CountDownLatch-Wert

                // Spiellogik implementieren und Nachrichten zwischen den Clients senden

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                removeClient(playerName);
            }
        }

        public void sendMessage(String message) {
            writer.println(message);
        }

        public void startGame() {
            writer.println("Das Spiel beginnt!");
        }
    }
}
