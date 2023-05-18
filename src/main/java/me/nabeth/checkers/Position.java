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
 * The {@code Position} class encapsulates a position on a checkers board in the
 * format of its row and column.
 */
public class Position {
    private int row, col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(Position other) {
        if (other != null) {
            this.row = other.row;
            this.col = other.col;
        }
    }

    public void reflect() {
        this.row = Board.SIZE - 1 - this.row;
        this.col = Board.SIZE - 1 - this.col;
    }

    /**
     * Assigns a discrete value in [0, 31] to valid positions from the
     * upper-left corner of the board.
     * 
     * @return a {@code byte} representing the number assigned to this position
     */
    public byte toByte() {
        return (byte) (4 * this.row + this.col / 2);
    }

    /**
     * Converts a {@code byte} representation of a position to a {@code Position}
     * instance.
     * 
     * @param num a {@code byte} representing the number assigned to a valid
     *            position
     * @return an instance of {@code Position} represented by the given byte
     */
    public static Position toPosition(byte num) {
        int row = num / 4;
        int col = 2 * (num % 4);
        if (row % 2 == 0) {
            col++;
        }
        return new Position(row, col);
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int hash = 7;
        hash = PRIME * hash + this.col;
        hash = PRIME * hash + this.row;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (this.col != other.col)
            return false;
        if (this.row != other.row)
            return false;
        return true;
    }
}