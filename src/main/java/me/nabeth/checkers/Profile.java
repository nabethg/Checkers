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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * The {@code Profile} class encapsulates a playing party's profile in the
 * format of its username, rating, and avatar image.
 */
public class Profile implements WritableToTxt, Visualizable, Serializable {
    private String username;
    private int rating;
    private BufferedImage avatar;

    public Profile(String username, int rating, BufferedImage avatar) {
        this.username = username;
        this.rating = rating;
        this.avatar = avatar;
    }

    public Profile() throws IOException {
        this.username = "Opponent";
        this.rating = 800;
        this.avatar = ImageIO.read(this.getClass().getResource(Resources.DEFAULT_AVATAR));
    }

    public Profile(String fileName) throws FileNotFoundException, IOException {
        Scanner input = new Scanner(new File(fileName));
        this.username = input.nextLine();
        this.rating = input.nextInt();
        this.avatar = ImageIO.read(this.getClass().getResource(Resources.PLAYER_AVATAR));
    }

    public String getUsername() {
        return this.username;
    }

    public int getRating() {
        return this.rating;
    }

    public BufferedImage getAvatar() {
        return this.avatar;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void updateRating(int oppRating, boolean playerWon) {
        int k; // standardized constant
        if (this.rating <= 2099) {
            k = 32;
        } else if (this.rating <= 2399) {
            k = 24;
        } else {
            k = 16;
        }
        int outcomeScore = playerWon ? 1 : 0;
        double winExpectancy = 1 / (Math.pow(10, -(this.rating - oppRating) / 400) + 1);
        this.rating += k * (outcomeScore - winExpectancy); // developed by Prof. Arpad Elo
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }

    @Override
    public void writeToTxt(String fileName) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new FileOutputStream(new File(fileName), false));
        output.println(this.username);
        output.println(this.rating);
        output.close();
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawImage(this.avatar, x, y, width, height, null);

        g2d.setColor(CheckersPanel.getTxtColor());
        g2d.setFont(new Font("Consolas", Font.PLAIN, 20));

        String label = String.format("%s%n%d", this.username, this.rating);
        int labelX = 2 * x + width;
        int labelY = y;
        for (String line : label.split(System.lineSeparator())) {
            g2d.drawString(line, labelX, labelY += g2d.getFontMetrics().getHeight());
        }
    }

    @Override
    public void writeObject(OutputStream output) throws IOException {
        ObjectOutputStream objOut = new ObjectOutputStream(output);
        objOut.writeObject(this.username);
        objOut.writeInt(this.rating);
        ImageIO.write(this.avatar, "png", objOut);
    }

    @Override
    public void readObject(InputStream input) throws IOException, ClassNotFoundException {
        ObjectInputStream objIn = new ObjectInputStream(input);
        this.username = (String) objIn.readObject();
        this.rating = objIn.readInt();
        this.avatar = ImageIO.read(objIn);
        while (objIn.available() > 0) {
            objIn.read();
        }
    }
}