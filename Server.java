import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * [Server.java]
 * A class that encapsulates a server within a network by handling its socket
 * and two clients' sockets.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class Server extends Thread {
    public static final int PORT = 5000;

    private boolean isRunning;
    private ServerSocket serverSocket;
    private Socket clientSocket1, clientSocket2;
    private Thread connectionHandler1, connectionHandler2;

    @Override
    public void run() {
        try {
            this.isRunning = true;

            this.serverSocket = new ServerSocket(PORT);
            this.clientSocket1 = this.serverSocket.accept();
            this.clientSocket2 = this.serverSocket.accept();

            this.connectionHandler1 = new ConnectionHandler(this.clientSocket1, this.clientSocket2);
            this.connectionHandler1.start();
            this.connectionHandler2 = new ConnectionHandler(this.clientSocket2, this.clientSocket1);
            this.connectionHandler2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() throws IOException {
        this.isRunning = false;
        this.serverSocket.close();
    }

    // inner class
    private class ConnectionHandler extends Thread {
        private Socket clientSocket1, clientSocket2;

        public ConnectionHandler(Socket socket1, Socket socket2) {
            this.clientSocket1 = socket1;
            this.clientSocket2 = socket2;
        }

        @Override
        public void run() {
            try {
                // send every byte sent by client1 to client2
                while (Server.this.isRunning) {
                    byte msg = (byte) this.clientSocket1.getInputStream().read();
                    this.clientSocket2.getOutputStream().write(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}