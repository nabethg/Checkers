import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * [Move.java]
 * A class that encapsulates a checkers move in the format of its source,
 * skipped, and destination position.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class Move implements Visualizable, Serializable {
    private Position src;
    private Position skip;
    private Position dest;

    public Move() {
    }

    public Move(Position src, Position dest) {
        this.src = src;
        this.dest = dest;
        if (this.isCapturing()) {
            this.skip = new Position((src.getRow() + dest.getRow()) / 2, (src.getCol() + dest.getCol()) / 2);
        }
    }

    public Move(Move other) {
        if (other != null) {
            this.src = new Position(other.src);
            this.skip = new Position(other.skip);
            this.dest = new Position(other.dest);
        }
    }

    /**
     * isCapturing
     * Returns whether or not a move captures an opponent piece.
     * 
     * @return true if the destination is two squares away from the source both
     *         horizontally and vertically.
     */
    public boolean isCapturing() {
        return (Math.abs(this.src.getRow() - this.dest.getRow()) == 2)
                && (Math.abs(this.src.getCol() - this.dest.getCol()) == 2);
    }

    public Position getSrc() {
        return this.src;
    }

    public Position getSkip() {
        return this.skip;
    }

    public Position getDest() {
        return this.dest;
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(x, y, width, height);
    }

    @Override
    public void writeObject(OutputStream output) throws IOException {
        output.write(this.src.toByte());
        output.write(this.dest.toByte());
    }

    @Override
    public void readObject(InputStream input) throws IOException, ClassNotFoundException {
        this.src = Position.toPosition((byte) input.read());
        this.dest = Position.toPosition((byte) input.read());
        if (this.isCapturing()) {
            this.skip = new Position((this.src.getRow() + this.dest.getRow()) / 2,
                    (this.src.getCol() + this.dest.getCol()) / 2);
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int hash = 7;
        hash = PRIME * hash + ((this.dest == null) ? 0 : this.dest.hashCode());
        hash = PRIME * hash + ((this.skip == null) ? 0 : this.skip.hashCode());
        hash = PRIME * hash + ((this.src == null) ? 0 : this.src.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Move other = (Move) obj;
        if (this.dest == null) {
            if (other.dest != null)
                return false;
        } else if (!this.dest.equals(other.dest))
            return false;
        if (this.skip == null) {
            if (other.skip != null)
                return false;
        } else if (!this.skip.equals(other.skip))
            return false;
        if (this.src == null) {
            if (other.src != null)
                return false;
        } else if (!this.src.equals(other.src))
            return false;
        return true;
    }
}