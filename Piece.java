import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * [Piece.java]
 * An enum that encapsulates all pieces in checkers.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public enum Piece implements Visualizable {
    PLAYER1_PAWN,
    PLAYER1_KING,
    PLAYER2_PAWN,
    PLAYER2_KING;

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        Image pieceImg = null;
        if (this == Piece.PLAYER1_PAWN) {
            pieceImg = Toolkit.getDefaultToolkit().getImage(Resources.PLAYER1_PAWN);
        } else if (this == Piece.PLAYER1_KING) {
            pieceImg = Toolkit.getDefaultToolkit().getImage(Resources.PLAYER1_KING);
        } else if (this == Piece.PLAYER2_PAWN) {
            pieceImg = Toolkit.getDefaultToolkit().getImage(Resources.PLAYER2_PAWN);
        } else if (this == Piece.PLAYER2_KING) {
            pieceImg = Toolkit.getDefaultToolkit().getImage(Resources.PLAYER2_KING);
        }
        g2d.drawImage(pieceImg, x, y, width, height, null);
    }
}