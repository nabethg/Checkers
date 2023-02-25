/**
 * [Position.java]
 * A class that encapsulates a position on a checkers board in the format of its
 * row and column.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
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
     * toByte
     * Assigns a number [0, 31] to the valid positions from the top left of the
     * board.
     * 
     * @return a byte representing the number assigned this position.
     */
    public byte toByte() {
        return (byte) (4 * this.row + this.col / 2);
    }

    /**
     * toPosition
     * Converts a byte representation of a position to a Position instance.
     * 
     * @param num a byte representing the number assigned to a valid position.
     * @return a Position represented by the given byte.
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