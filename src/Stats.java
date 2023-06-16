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
        String name = player.getName();
        int gewonnene = gewonneneSpiele.getOrDefault(name, 0);
        gewonneneSpiele.put(name, gewonnene + 1);
    }

    private void erhoeheVerloreneSpiele(Player player) {
        String name = player.getName();
        int verlorene = verloreneSpiele.getOrDefault(name, 0);
        verloreneSpiele.put(name, verlorene + 1);
    }

    public int getGewonneneSpiele(Player player) {
        String name = player.getName();
        return gewonneneSpiele.getOrDefault(name, 0);
    }

    public int getVerloreneSpiele(Player player) {
        String name = player.getName();
        return verloreneSpiele.getOrDefault(name, 0);
    }

    public void setGewonneneSpiele(Player player, int gewonnene) {
        String name = player.getName();
        gewonneneSpiele.put(name, gewonnene);
    }
}
