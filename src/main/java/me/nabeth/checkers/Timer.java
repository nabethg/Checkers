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

import java.awt.Font;
import java.awt.Graphics2D;

/**
 * The {@code Timer} class encapsulates a checkers timer by keeping track of
 * time when it is unpaused.
 */
public class Timer extends Thread implements Visualizable {
    private Checkers game;
    private long timeLeft; // from last unpause
    private long displayTime;
    private boolean isRunning;

    public Timer(Checkers game, long duration) {
        this.game = game;
        this.timeLeft = duration;
        this.displayTime = duration;
        this.isRunning = false;
    }

    @Override
    public void run() {
        while (this.timeLeft > 0) {
            Thread.yield();
            if (this.isRunning) {
                long initTime = System.currentTimeMillis();
                long currTime = System.currentTimeMillis();
                while ((currTime - initTime <= this.timeLeft) && (this.isRunning)) {
                    Thread.yield();
                    currTime = System.currentTimeMillis();
                    this.displayTime = this.timeLeft - (currTime - initTime);
                }
                this.timeLeft -= currTime - initTime;
            }
        }
        this.game.getGameRunner().endGame();
    }

    public long getTimeLeft() {
        return this.timeLeft;
    }

    public void pause() {
        this.isRunning = false;
    }

    public void unpause() {
        this.isRunning = true;
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        // draw box
        g2d.setColor(CheckersPanel.getShapeColor());
        g2d.fillRect(x, y, width, height);

        // draw timer value
        g2d.setColor(CheckersPanel.getTxtColor());
        g2d.setFont(new Font("Consolas", Font.PLAIN, 35));
        int labelX = x + (width - g2d.getFontMetrics().stringWidth(this.toString())) / 2;
        int labelY = y + g2d.getFontMetrics().getHeight();
        g2d.drawString(this.toString(), labelX, labelY);
    }

    @Override
    public String toString() {
        long minutes = this.displayTime / (60 * 1000);
        long seconds = this.displayTime % (60 * 1000) / 1000;
        return String.format("%d:%d%d", minutes, seconds / 10, seconds % 10);
    }
}