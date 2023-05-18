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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * The {@code Settings} class encapsulates a collection of settings parameters
 * including theme, sound effects, AI difficulty, time limit, and the player's
 * pieces' colour.
 */
public class Settings implements WritableToTxt, Serializable {
    public static final int NATIVE_THEME = 0, DARK_THEME = 1, LIGHT_THEME = 2;
    public static final int MIN_AI_DIFFICULTY = 1, MAX_AI_DIFFICULTY = 8;
    public static final int[] TIME_LIMITS = { 1, 2, 3, 5, 10, 30 };

    private int theme;
    private boolean hasSoundEffects;
    private int AIDifficulty;
    private int timeLimitIndex;
    private boolean playAsBlack;

    public Settings() {
    }

    public Settings(String fileName) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        this.theme = input.nextInt();
        this.hasSoundEffects = input.nextBoolean();
        this.AIDifficulty = input.nextInt();
        this.timeLimitIndex = input.nextInt();
        this.playAsBlack = input.nextBoolean();
    }

    public int getTheme() {
        return this.theme;
    }

    public boolean hasSoundEffects() {
        return this.hasSoundEffects;
    }

    public int getAIDifficulty() {
        return this.AIDifficulty;
    }

    public int getTimeLimitIndex() {
        return this.timeLimitIndex;
    }

    /**
     * Returns the time limit indicated by this {@code Setting} instance based on
     * its index within the list of available options.
     * 
     * @return a {@code long} representing the time limit in milliseconds.
     */
    public long getTimeMilli() {
        return 1000 * 60 * Settings.TIME_LIMITS[this.timeLimitIndex];
    }

    public boolean PlayAsBlack() {
        return this.playAsBlack;
    }

    /**
     * Returns the player associated with this {@code Setting} instance based on the
     * player's pieces' colour.
     * 
     * @return a {@code Player} representing the player.
     */
    public Player getPlayer() {
        if (this.playAsBlack == true) {
            return Player.PLAYER1;
        }
        return Player.PLAYER2;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public void setHasSoundEffects(boolean hasSoundEffects) {
        this.hasSoundEffects = hasSoundEffects;
    }

    public void setAIDifficulty(int AIDifficulty) {
        this.AIDifficulty = AIDifficulty;
    }

    public void setTimeLimitIndex(int timeLimitIndex) {
        this.timeLimitIndex = timeLimitIndex;
    }

    public void setPlayAsBlack(boolean goesFirst) {
        this.playAsBlack = goesFirst;
    }

    @Override
    public void writeToTxt(String fileName) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new FileOutputStream(new File(fileName), false));
        output.println(this.theme);
        output.println(this.hasSoundEffects);
        output.println(this.AIDifficulty);
        output.println(this.timeLimitIndex);
        output.println(this.playAsBlack);
        output.close();
    }

    @Override
    public void writeObject(OutputStream output) throws IOException {
        output.write(this.timeLimitIndex);
        byte playAsBlack = (byte) ((this.playAsBlack == true) ? 0 : 1);
        output.write(playAsBlack);
    }

    @Override
    public void readObject(InputStream input) throws IOException, ClassNotFoundException {
        this.timeLimitIndex = input.read();
        this.playAsBlack = (input.read() == 1);
    }
}