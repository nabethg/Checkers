/*
 * Checkers - multi-platform desktop checkers program
 * Copyright © 2023 Nabeth Ghazi
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.nabeth.checkers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The {@code Server} class encapsulates a server within a network by handling
 * its socket and two clients' sockets.
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