import java.io.IOException;
import java.net.Socket;

/**
 * [Client.java]
 * A class that encapsulates a client within a network by handling its socket.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class Client {
    private Socket clientSocket;

    public Client(String localHost) throws IOException {
        this.clientSocket = new Socket(localHost, Server.PORT);
    }

    public void write(Serializable obj) throws IOException {
        obj.writeObject(this.clientSocket.getOutputStream());
    }

    private Serializable read(Serializable obj) throws IOException, ClassNotFoundException {
        obj.readObject(this.clientSocket.getInputStream());
        return obj;
    }

    public Settings readSettings() throws IOException, ClassNotFoundException {
        return (Settings) this.read(new Settings());
    }

    public Profile readProfile() throws IOException, ClassNotFoundException {
        return (Profile) this.read(new Profile());
    }

    public Move readMove() throws IOException, ClassNotFoundException {
        return (Move) this.read(new Move());
    }
}