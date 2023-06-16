import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Player {
    private String name;
    private int spielzug;
    private String antwort;
    private CountDownLatch nameEingabeLatch;
    private CountDownLatch spielzugEingabeLatch;
    private CountDownLatch antwortEingabeLatch;

    public Player() {
        this.nameEingabeLatch = new CountDownLatch(1);
        this.spielzugEingabeLatch = new CountDownLatch(1);
        this.antwortEingabeLatch = new CountDownLatch(1);
    }

    public void warteAufNamen() {
        System.out.println("Warte auf Namen des Spielers...");
        try {
            nameEingabeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void warteAufSpielzug() {
        System.out.println("Warte auf Spielzug...");
        try {
            spielzugEingabeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void warteAufAntwort() {
        System.out.println("Warte auf Antwort...");
        try {
            antwortEingabeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public int getSpielzug() {
        return spielzug;
    }

    public String getAntwort() {
        return antwort;
    }

    // Aufruf dieser Methode, um den Namen des Spielers festzulegen
    public void setName(String name) {
        this.name = name;
        nameEingabeLatch.countDown();
    }

    // Aufruf dieser Methode, um den Spielzug des Spielers festzulegen
    public void setSpielzug(int spielzug) {
        this.spielzug = spielzug;
        spielzugEingabeLatch.countDown();
    }

    // Aufruf dieser Methode, um die Antwort des Spielers festzulegen
    public void setAntwort(String antwort) {
        this.antwort = antwort;
        antwortEingabeLatch.countDown();
    }
}