import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private String name;

    public Player(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void flush(){
        writer.flush();
    }
    public void send(String message) {
        writer.println(message);
    }

    public String receive() throws IOException {
        return reader.readLine();
    }

    public String receiveAsync() throws IOException {
        final String[] inputHolder = new String[1]; // Array zum Halten der Eingabe

        Thread thread = new Thread(() -> {
            try {
                inputHolder[0] = reader.readLine(); // Asynchrone Leseoperation
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start(); // Starten des Hintergrundthreads
        try {
            thread.join(); // Warten auf Abschluss des Hintergrundthreads
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return inputHolder[0];
    }


    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}
