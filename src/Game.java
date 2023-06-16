import java.io.IOException;

public class Game {
    private final Player player1;
    private final Player player2;
    private final Stats stats;

    public Game(Player player1, Player player2, Stats stats) {
        this.player1 = player1;
        this.player2 = player2;
        this.stats = stats;
    }

    public void start() {
        try {
            int spielzug1 = warteAufSpielzug(player1);

            int spielzug2 = warteAufSpielzug(player2);

            player1.send("Spielzug von " + player2.getName() + ": " + spielzug2);
            player2.send("Spielzug von " + player1.getName() + ": " + spielzug1);
            player1.flush();
            player2.flush();
            String gewinner = ermittleGewinner(spielzug1, spielzug2);
            player1.send("Gewinner: " + gewinner);
            player2.send("Gewinner: " + gewinner);
            player1.flush();
            player2.flush();
            stats.aktualisiereStats(player1, player2, gewinner);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                player1.close();
                player2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int warteAufSpielzug(Player player) throws IOException {
        player.send("Gib deinen Spielzug ein (1 = Schere, 2 = Stein, 3 = Papier):");
        player.flush();
        try {
            String input = player.receiveAsync();
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            player.send("Ungültige Eingabe. Versuche es erneut.");
            player.flush();
            return warteAufSpielzug(player);
        }
    }

    private String ermittleGewinner(int spielzug1, int spielzug2) {
        if (spielzug1 == spielzug2) {
            return "Unentschieden";
        } else if ((spielzug1 == 1 && spielzug2 == 3) || (spielzug1 == 2 && spielzug2 == 1) || (spielzug1 == 3 && spielzug2 == 2)) {
            return player1.getName();
        } else {
            return player2.getName();
        }
    }
}
