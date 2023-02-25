/**
 * [Player.java]
 * An enum that encapsulates the two playing parties in a checkers game.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
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