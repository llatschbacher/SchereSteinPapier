import java.util.concurrent.CountDownLatch;

public class Player {
    private String name;
    private int spielzug;
    private CountDownLatch nameEingabeLatch;
    private CountDownLatch spielzugEingabeLatch;

    public Player() {
        nameEingabeLatch = new CountDownLatch(1);
        spielzugEingabeLatch = new CountDownLatch(1);
    }

    public void setName(String name) {
        this.name = name;
        nameEingabeLatch.countDown();
    }

    public String getName() {
        return this.name;
    }

    public void setSpielzug(int spielzug) {
        this.spielzug = spielzug;
        spielzugEingabeLatch.countDown();
    }

    public int getSpielzug() {
        return spielzug;
    }

    public void warteAufNamen() {
        try {
            nameEingabeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void warteAufSpielzug() {
        try {
            spielzugEingabeLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}