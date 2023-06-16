import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Verbindung hergestellt, bitte Namen eingeben");
            // Lese die Nachricht vom Server
            String message = input.readLine();
            System.out.println("Server: " + message);

            // Eingabe des Spielers
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Frage nach dem Namen des Spielers
            System.out.print("Wie heißt du? ");
            String playerName = userInput.readLine();
            output.println(playerName);

            // Warte auf Verbindungsinformationen vom Server
            message = input.readLine();
            System.out.println("Server: " + message);

            // Warte auf die Spielzüge und Ergebnisse
            while (true) {
                // Frage nach dem Spielzug
                message = input.readLine();
                System.out.println("Server: " + message);
                int move = Integer.parseInt(userInput.readLine());
                output.println(move);

                // Erhalte und zeige das Ergebnis
                message = input.readLine();
                System.out.println("Server: " + message);

                // Frage nach einer erneuten Spielrunde
                message = input.readLine();
                System.out.println("Server: " + message);
                String playAgain = userInput.readLine();
                output.println(playAgain);

                if (!playAgain.equalsIgnoreCase("ja")) {
                    break;
                }
            }

            // Schließe die Verbindung
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
