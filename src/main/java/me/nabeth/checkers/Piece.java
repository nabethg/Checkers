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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * The {@code Piece} enum that encapsulates the four types of pieces in
 * checkers.
 */
public enum Piece implements Visualizable {
    PLAYER1_PAWN,
    PLAYER1_KING,
    PLAYER2_PAWN,
    PLAYER2_KING;

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        String pieceFileName;
        switch (this) {
            case PLAYER1_PAWN:
                pieceFileName = Resources.PLAYER1_PAWN;
                break;
            case PLAYER1_KING:
                pieceFileName = Resources.PLAYER1_KING;
                break;
            case PLAYER2_PAWN:
                pieceFileName = Resources.PLAYER2_PAWN;
                break;
            default:
                pieceFileName = Resources.PLAYER2_KING;
                break;
        }
        Image pieceImg = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(pieceFileName));
        g2d.drawImage(pieceImg, x, y, width, height, null);
    }
}