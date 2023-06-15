public class Game {
    private final Player player1;
    private final Player player2;
    private String gewinner;
    private final Stats stats;

    public Game(Player player1, Player player2, Stats stats) {
        this.player1 = player1;
        this.player2 = player2;
        this.stats = stats;
    }

    public void start() {
        wartenAufNamen();
        wartenAufSpielzug();
        ermittelnUndAnzeigenDesGewinners();
        aktualisierenDerStats();
    }

    private void wartenAufNamen() {
        // Warten bis beide Player ihre Namen eingegeben haben
        player1.warteAufNamen();
        player2.warteAufNamen();
        System.out.println("Du bist mit " + player1.getName() + " verbunden!");
        System.out.println("Du bist mit " + player2.getName() + " verbunden!");
    }

    private void wartenAufSpielzug() {
        // Warten bis beide Player ihren Spielzug gemacht haben
        player1.warteAufSpielzug();
        player2.warteAufSpielzug();
    }

    private void ermittelnUndAnzeigenDesGewinners() {
        int spielzugPlayer1 = player1.getSpielzug();
        int spielzugPlayer2 = player2.getSpielzug();

        //(0/Schere/1/Stein/2/Papier)
        gewinner = "";
        switch (spielzugPlayer1) {
            case 0:
                if (spielzugPlayer2 == 0) {
                    gewinner = "Keiner";
                    break;
                } else if (spielzugPlayer2 == 1) {
                    gewinner = player2.getName();
                    break;
                } else if (spielzugPlayer2 == 2) {
                    gewinner = player1.getName();
                    break;
                }
                break;

            case 1:
                if (spielzugPlayer2 == 0) {
                    gewinner = player1.getName();
                    break;
                } else if (spielzugPlayer2 == 1) {
                    gewinner = "Keiner";
                    break;
                } else if (spielzugPlayer2 == 2) {
                    gewinner = player2.getName();
                    break;
                }
                break;

            case 2:
                if (spielzugPlayer2 == 0) {
                    gewinner = player2.getName();
                    break;
                } else if (spielzugPlayer2 == 1) {
                    gewinner = player1.getName();
                    break;
                } else if (spielzugPlayer2 == 2) {
                    gewinner = "Keiner";
                    break;
                }
                break;

            default:
                break;
        }

                System.out.println(player1.getName() + " spielt " + getSpielzugName(spielzugPlayer1) + ", " + player2.getName() + " spielt " + getSpielzugName(spielzugPlayer2) + ", " + gewinner + " gewinnt!");
    }

    private String getSpielzugName(int spielzug) {
        //(0/Schere/1/Stein/2/Papier)
        switch(spielzug){
            case 0:
                return "Schere";
            case 1:
                return "Stein";
            case 2:
                return "Papier";
            default:
                return "";
        }
    }

    private void aktualisierenDerStats() {
        stats.aktualisiereStats(player1, player2, gewinner);
        System.out.println(player1.getName() + " hat bisher " + stats.getGewonneneSpiele(player1) +
                " mal gewonnen und " + stats.getVerloreneSpiele(player1) + " mal verloren!");
        System.out.println(player2.getName() + " hat bisher " + stats.getGewonneneSpiele(player2) +
                " mal gewonnen und " + stats.getVerloreneSpiele(player2) + " mal verloren!");
    }
}