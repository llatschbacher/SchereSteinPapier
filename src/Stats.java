import java.util.HashMap;
import java.util.Map;

public class Stats {
    private final Map<String, Integer> gewonneneSpiele;
    private final Map<String, Integer> verloreneSpiele;

    public Stats() {
        gewonneneSpiele = new HashMap<>();
        verloreneSpiele = new HashMap<>();
    }

    public void aktualisiereStats(Player player1, Player player2, String gewinner) {
        if (gewinner.equals(player1.getName())) {
            erhoeheGewonneneSpiele(player1);
            erhoeheVerloreneSpiele(player2);
        } else if (gewinner.equals(player2.getName())) {
            erhoeheGewonneneSpiele(player2);
            erhoeheVerloreneSpiele(player1);
        }
    }

    private void erhoeheGewonneneSpiele(Player player) {
        gewonneneSpiele.put(player.getName(), gewonneneSpiele.getOrDefault(player.getName(), 0) + 1);
    }

    private void erhoeheVerloreneSpiele(Player player) {
        verloreneSpiele.put(player.getName(), verloreneSpiele.getOrDefault(player.getName(), 0) + 1);
    }

    public int getGewonneneSpiele(Player player) {
        return gewonneneSpiele.getOrDefault(player.getName(), 0);
    }

    public int getVerloreneSpiele(Player player) {
        return verloreneSpiele.getOrDefault(player.getName(), 0);
    }
}