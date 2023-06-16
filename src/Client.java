import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Klasse die bei einer TCP Kommunikation den Client und seine Funktionen darstellt
 *
 * @author llatschbacher
 * @version 2023-06-16
 */
public class Client {
    private final String serverIp;
    private final int serverPort;

    public Client(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            // Eine Verbindung zum Server herstellen
            Socket socket = new Socket(serverIp, serverPort);

            // Eingabestrom für den Empfang von Daten vom Server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Ausgabestrom für das Senden von Daten an den Server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Eingabestrom für Benutzereingaben von der Konsole
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Den Benutzernamen abfragen und an den Server senden
            System.out.println("Gib deinen Namen ein:");
            String playerName = consoleReader.readLine();
            out.println(playerName);

            // Den Namen des anderen Spielers vom Server erhalten und anzeigen
            String otherPlayerName = in.readLine();
            if (otherPlayerName != null) {
                System.out.println(otherPlayerName);
            }

            // Die Spielschleife starten
            while (true) {
                // Spielzug-Anfrage vom Server erhalten und anzeigen
                String spielzugAnfrage = in.readLine();
                System.out.println(spielzugAnfrage);

                // Den Spielzug vom Benutzer lesen und an den Server senden
                int spielzug = Integer.parseInt(consoleReader.readLine());
                out.println(spielzug);

                // Die Antwort des Servers erhalten und anzeigen
                String antwort = in.readLine();
                System.out.println(antwort);

                // Die Antwort des Spielers lesen und an den Server senden
                String spielerAntwort = consoleReader.readLine();
                out.println(spielerAntwort);

                // Den Gewinner des Spiels vom Server erhalten und anzeigen
                String gewinner = in.readLine();
                System.out.println("Gewinner: " + gewinner);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Bitte geben Sie die IP-Adresse und den Port als Argumente an.");
            return;
        }

        String serverIp = args[0];
        int serverPort = Integer.parseInt(args[1]);

        // Erstelle eine neue Client-Instanz und starte den Client
        Client client = new Client(serverIp, serverPort);
        client.start();
    }
}
