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
import java.net.Socket;

/**
 * The {@code Client} class encapsulates a client within a network by handling
 * its socket.
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