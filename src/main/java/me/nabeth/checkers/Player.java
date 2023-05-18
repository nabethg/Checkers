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

/**
 * The {@code Player} enum is used to encapsulate the two playing parties in a
 * checkers game.
 */
public enum Player {
    PLAYER1,
    PLAYER2;

    public Player getOpponent() {
        if (this == Player.PLAYER1) {
            return Player.PLAYER2;
        } else if (this == Player.PLAYER2) {
            return Player.PLAYER1;
        }
        return null;
    }

    public Piece getPawn() {
        if (this == Player.PLAYER1) {
            return Piece.PLAYER1_PAWN;
        } else if (this == Player.PLAYER2) {
            return Piece.PLAYER2_PAWN;
        }
        return null;
    }

    public Piece getKing() {
        if (this == Player.PLAYER1) {
            return Piece.PLAYER1_KING;
        } else if (this == Player.PLAYER2) {
            return Piece.PLAYER2_KING;
        }
        return null;
    }

    @Override
    public String toString() {
        if (this == Player.PLAYER1) {
            return "Black";
        } else if (this == Player.PLAYER2) {
            return "Red";
        }
        return null;
    }
}