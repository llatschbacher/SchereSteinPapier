import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final String serverIp;
    private final int serverPort;

    public Client(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            Socket socket = new Socket(serverIp, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Gib deinen Namen ein:");
            String playerName = consoleReader.readLine();
            out.println(playerName);

            String otherPlayerName = in.readLine();
            if (otherPlayerName != null) {
                System.out.println(otherPlayerName);
            }

            while (true) {
                String spielzugAnfrage = in.readLine();
                System.out.println(spielzugAnfrage);

                int spielzug = Integer.parseInt(consoleReader.readLine());
                out.println(spielzug);

                String antwort = in.readLine();
                System.out.println(antwort);

                String spielerAntwort = consoleReader.readLine();
                out.println(spielerAntwort);

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

        Client client = new Client(serverIp, serverPort);
        client.start();
    }
}
