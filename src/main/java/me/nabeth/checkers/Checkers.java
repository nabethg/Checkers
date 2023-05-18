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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

/**
 * The {@code Checkers} class encapsulates a virtual checkers game by handling
 * game components such as a network, settings, player, board, timers, profiles,
 * and a visualizer.
 */
public class Checkers {
    public static final String VERSION = "1.0";

    private Client client;
    private Server server;
    private Settings playerSettings;
    private Player player;
    private Board board;
    private Timer playerTimer, oppTimer;
    private AI AIPlayer;
    private Profile playerProfile, oppProfile;
    private Visualizer visualizer;
    private GameRunner gameRunner;
    private Boolean isPvP;

    public static void main(String[] args) {
        new Checkers();
    }

    public Checkers() {
        try {
            this.playerSettings = new Settings(Resources.PATH + Resources.PLAYER_SETTINGS);
            this.setGameParams(this.playerSettings);
            this.AIPlayer = new AI(this.playerSettings.getAIDifficulty());
            this.playerProfile = new Profile(Resources.PATH + Resources.PLAYER_PROFILE);
            this.visualizer = new Visualizer(this);
        } catch (FileNotFoundException e) {
            Visualizer.showMessage(null, "Error", "Resource File Not Found!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            Visualizer.showMessage(null, "Error", "Failed to perform IO operation!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setGameParams(Settings gameSettings) {
        this.player = gameSettings.getPlayer();
        this.board = new Board(this.player);
        this.playerTimer = new Timer(this, gameSettings.getTimeMilli());
        this.oppTimer = new Timer(this, gameSettings.getTimeMilli());
    }

    public void showIOExceptionMessage() {
        this.visualizer.showMessage("Error", "Failed to perform IO operation!", JOptionPane.ERROR_MESSAGE);
    }

    public void showClassNotFoundExceptionMessage() {
        this.visualizer.showMessage("Error", "Failed to transfer player data!", JOptionPane.ERROR_MESSAGE);
    }

    public void resetGame() throws IOException {
        if (this.server != null) {
            this.server.terminate();
            this.server = null;
        }
        this.client = null;
        if (this.inGame()) {
            this.playerTimer.stop();
            this.oppTimer.stop();
            this.gameRunner.stop();
            this.gameRunner = null;
        }
    }

    public void hostPvPGame() {
        try {
            this.resetGame();
            this.isPvP = true;

            this.server = new Server();
            this.server.start();

            String localHost = Inet4Address.getLocalHost().getHostAddress();
            this.client = new Client(localHost);
            this.visualizer.showMessage("Host IP Address",
                    String.format("Share your IP (%s) with your opponent.", localHost),
                    JOptionPane.INFORMATION_MESSAGE);

            this.client.write(this.playerSettings);
            this.setGameParams(this.playerSettings);

            this.gameRunner = new GameRunner(this);
            this.gameRunner.start();
        } catch (UnknownHostException e) {
            this.visualizer.showMessage("Error", "Failed to host PvP game!",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            this.showIOExceptionMessage();
        }
    }

    public void joinPvPGame() {
        try {
            this.resetGame();
            this.isPvP = true;

            String localHost = this.visualizer.showInputPrompt("Host IP Address",
                    "Please enter the host's IP address:");
            if (localHost == null) {
                return;
            }
            this.client = new Client(localHost);

            this.setGameParams(this.client.readSettings());

            this.gameRunner = new GameRunner(this);
            this.gameRunner.start();
        } catch (IOException e) {
            this.showIOExceptionMessage();
        } catch (ClassNotFoundException e) {
            this.showClassNotFoundExceptionMessage();
        }
    }

    public void newPvAIGame() {
        try {
            this.resetGame();
            this.isPvP = false;

            this.setGameParams(this.playerSettings);

            this.gameRunner = new GameRunner(this);
            this.gameRunner.start();
        } catch (IOException e) {
            this.showIOExceptionMessage();
        }
    }

    public Client getClient() {
        return this.client;
    }

    public Settings getPlayerSettings() {
        return this.playerSettings;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Board getBoard() {
        return this.board;
    }

    public Timer getPlayerTimer() {
        return this.playerTimer;
    }

    public Timer getOppTimer() {
        return this.oppTimer;
    }

    public AI getAIPlayer() {
        return this.AIPlayer;
    }

    public Profile getPlayerProfile() {
        return this.playerProfile;
    }

    public Profile getOppProfile() throws IOException {
        if (this.isPvP == null) {
            return new Profile();
        } else if (this.isPvP == true) {
            if (this.oppProfile == null) {
                return new Profile();
            }
            return this.oppProfile;
        }
        return this.AIPlayer.getProfile();
    }

    public Visualizer getVisualizer() {
        return this.visualizer;
    }

    public GameRunner getGameRunner() {
        return this.gameRunner;
    }

    public boolean inGame() {
        return (this.gameRunner != null);
    }

    public boolean isPvP() {
        return this.isPvP;
    }

    public void setAIPlayer(AI AIPlayer) {
        this.AIPlayer = AIPlayer;
    }

    public void setPlayerProfile(Profile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public void setOppProfile(Profile oppProfile) {
        this.oppProfile = oppProfile;
    }

    public void setVisualizer(Visualizer visualizer) {
        this.visualizer = visualizer;
    }
}