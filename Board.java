import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * [Board.java]
 * A class that encapsulates a checkers board in the format of its contained
 * pieces, player, and positions/moves selected.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class Board implements Visualizable {
    public static final int SIZE = 8;
    public static final int SQUARE_SIZE = 90;
    private static final int RADIUS = 35;

    private Piece[][] board;
    private Map<Piece, Integer> counts;
    private static Player player;
    private Player playerTurn;
    private Position selected;
    private Position lastDest;
    private Move playedMove;

    public Board(Player player) {
        this.board = new Piece[Board.SIZE][Board.SIZE];
        for (int col = 1; col < Board.SIZE; col += 2) {
            this.board[0][col] = Player.PLAYER2.getPawn();
            this.board[2][col] = Player.PLAYER2.getPawn();
            this.board[6][col] = Player.PLAYER1.getPawn();
        }
        for (int col = 0; col < Board.SIZE; col += 2) {
            this.board[1][col] = Player.PLAYER2.getPawn();
            this.board[5][col] = Player.PLAYER1.getPawn();
            this.board[7][col] = Player.PLAYER1.getPawn();
        }
        this.counts = new HashMap<>();
        this.counts.put(Player.PLAYER1.getPawn(), 12);
        this.counts.put(Player.PLAYER2.getPawn(), 12);
        Board.player = player;
        this.playerTurn = Player.PLAYER1;
    }

    public Board(Board other) {
        if (other != null) {
            this.board = new Piece[Board.SIZE][Board.SIZE];
            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    this.board[row][col] = other.board[row][col];
                }
            }
            this.counts = new HashMap<>();
            this.counts.putAll(other.counts);
            this.playerTurn = other.playerTurn;
            this.selected = new Position(other.selected);
            this.lastDest = new Position(other.lastDest);
            this.playedMove = new Move(other.playedMove);
        }
    }

    /**
     * heuristic
     * Evaluates the state of this board based on the number of each type of piece
     * for each player.
     * 
     * @return an int representing the heuristic evaluation of this board.
     */
    public int heuristic() {
        int eval = 2 * this.counts.getOrDefault(Player.PLAYER1.getPawn(), 0)
                - 2 * this.counts.getOrDefault(Player.PLAYER2.getPawn(), 0)
                + 3 * this.counts.getOrDefault(Player.PLAYER1.getKing(), 0)
                - 3 * this.counts.getOrDefault(Player.PLAYER2.getKing(), 0);
        if (Board.player.getOpponent() == Player.PLAYER2) {
            eval *= -1; // positive if opponent is winning
        }
        return eval;
    }

    private boolean isValidPosition(Position pos) {
        return ((pos.getRow() >= 0) && (pos.getRow() < Board.SIZE) && (pos.getCol() >= 0)
                && (pos.getCol() < Board.SIZE));
    }

    private boolean isValidMove(Move move) {
        if (this.isValidPosition(move.getDest()) && (this.getPiece(move.getDest()) == null)) {
            Piece srcPiece = this.getPiece(move.getSrc());
            if (srcPiece == this.playerTurn.getPawn()) {
                if ((srcPiece == Player.PLAYER1.getPawn()) && (move.getSrc().getRow() > move.getDest().getRow())) {
                    return true; // player 1 pawn
                }
                if ((srcPiece == Player.PLAYER2.getPawn()) && (move.getSrc().getRow() < move.getDest().getRow())) {
                    return true; // player 2 pawn
                }
            }
            if (srcPiece == this.playerTurn.getKing()) {
                return true; // king
            }
        }
        return false;
    }

    private boolean isValidCapture(Move capture) {
        if (!this.isValidPosition(capture.getSkip())) {
            return false;
        }
        Piece skipPiece = this.getPiece(capture.getSkip());
        return (this.isValidMove(capture)) && ((skipPiece == this.playerTurn.getOpponent().getPawn())
                || (skipPiece == this.playerTurn.getOpponent().getKing()));
    }

    private Set<Move> genOrdinaryMoves(Position src) {
        Set<Move> ordinaryMoves = new HashSet<>();
        int[][] ordinaryDirs = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
        for (int i = 0; i < ordinaryDirs.length; i++) {
            Position dest = new Position(src.getRow() + ordinaryDirs[i][0], src.getCol() + ordinaryDirs[i][1]);
            Move ordinaryMove = new Move(src, dest);
            if (this.isValidMove(ordinaryMove)) {
                ordinaryMoves.add(ordinaryMove);
            }
        }
        return ordinaryMoves;
    }

    private Set<Move> genOrdinaryMoves() {
        Set<Move> ordinaryMoves = new HashSet<>();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Position src = new Position(row, col);
                ordinaryMoves.addAll(this.genOrdinaryMoves(src));
            }
        }
        return ordinaryMoves;
    }

    private Set<Move> genCapturingMoves(Position src) {
        Set<Move> capturingMoves = new HashSet<>();
        int[][] capturingDirs = { { 2, 2 }, { 2, -2 }, { -2, 2 }, { -2, -2 } };
        for (int i = 0; i < capturingDirs.length; i++) {
            Position dest = new Position(src.getRow() + capturingDirs[i][0], src.getCol() + capturingDirs[i][1]);
            Move capturingMove = new Move(src, dest);
            if (this.isValidCapture(capturingMove)) {
                capturingMoves.add(capturingMove);
            }
        }
        return capturingMoves;
    }

    private Set<Move> genCapturingMoves() {
        Set<Move> capturingMoves = new HashSet<>();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Position src = new Position(row, col);
                capturingMoves.addAll(this.genCapturingMoves(src));
            }
        }
        return capturingMoves;
    }

    private Set<Move> getLegalMoves(Position src) {
        // multiple captures
        if ((this.lastDest != null) && (src.equals(this.lastDest))) {
            return this.genCapturingMoves(src);
        }

        Set<Move> legalMoves = new HashSet<>();
        // captures from source
        legalMoves.addAll(this.genCapturingMoves(src));
        if (!legalMoves.isEmpty()) {
            return legalMoves;
        }
        // captures from other sources
        if (!this.genCapturingMoves().isEmpty()) {
            return legalMoves;
        }
        // ordinary moves
        legalMoves.addAll(this.genOrdinaryMoves(src));
        return legalMoves;
    }

    public Set<Move> getLegalMoves() {
        Set<Move> legalMoves = new HashSet<>();
        // captures
        legalMoves.addAll(this.genCapturingMoves());
        if (!legalMoves.isEmpty()) {
            return legalMoves;
        }
        // ordinary moves
        legalMoves.addAll(this.genOrdinaryMoves());
        return legalMoves;
    }

    private void promote(Position pos) {
        if (this.getPiece(pos) == Player.PLAYER1.getPawn()) {
            this.setPiece(pos, Player.PLAYER1.getKing());
        } else if (this.getPiece(pos) == Player.PLAYER2.getPawn()) {
            this.setPiece(pos, Player.PLAYER2.getKing());
        }
    }

    public void perform(Move move) {
        // move friendly piece
        this.setPiece(move.getDest(), this.getPiece(move.getSrc()));
        this.setPiece(move.getSrc(), null);

        // remove opponent piece
        if (move.isCapturing()) {
            int count = this.counts.getOrDefault(this.getPiece(move.getSkip()), 0);
            this.counts.put(this.getPiece(move.getSkip()), count - 1);
            this.setPiece(move.getSkip(), null);
        }

        // promote pawn to king
        if (((move.getDest().getRow() == 0) && (this.getPiece(move.getDest()) == Player.PLAYER1.getPawn()))
                || ((move.getDest().getRow() == Board.SIZE - 1)
                        && (this.getPiece(move.getDest()) == Player.PLAYER2.getPawn()))) {
            this.promote(move.getDest());
        }

        // change player turn
        boolean playerCanCapture = false;
        for (Move legalMove : this.getLegalMoves(move.getDest())) {
            if (legalMove.isCapturing()) {
                playerCanCapture = true;
            }
        }
        if (move.isCapturing() && playerCanCapture) {
            this.lastDest = move.getDest();
        } else {
            this.playerTurn = this.playerTurn.getOpponent();
            this.lastDest = null;
        }
    }

    public Player getWinner() {
        if (this.getLegalMoves().isEmpty()) {
            return this.playerTurn.getOpponent();
        }
        return null;
    }

    public boolean gameIsOver() {
        return this.getWinner() != null;
    }

    /**
     * select
     * Marks a clicked piece as selected.
     * 
     * @return true if the a piece was clicked, otherwise false.
     */
    public boolean select(int mouseX, int mouseY) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                // check if click was within the piece
                int pieceX = (int) ((col + 0.5) * Board.SQUARE_SIZE);
                int pieceY = (int) ((row + 0.5) * Board.SQUARE_SIZE);
                int dist = (int) (Math.sqrt(Math.pow(mouseX - pieceX, 2) + Math.pow(mouseY - pieceY, 2)));
                if ((dist <= Board.RADIUS) && (this.board[row][col] != null)) {
                    this.selected = new Position(row, col);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * validateMove
     * Stores a legal move that was selected by a mouse click and resets the
     * previously selected position.
     */
    public void validateMove(int mouseX, int mouseY) {
        int row = mouseY / Board.SQUARE_SIZE;
        int col = mouseX / Board.SQUARE_SIZE;

        if ((mouseX >= 0) && (mouseY >= 0) && (row < Board.SIZE) && (col < Board.SIZE) && (this.selected != null)) {
            Move move = new Move(this.selected, new Position(row, col));
            Set<Move> legalMoves = this.getLegalMoves(this.selected);
            if (legalMoves.contains(move)) {
                this.playedMove = move;
            }
        }
        this.selected = null;
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        int margin = Board.SQUARE_SIZE / 2 - Board.RADIUS;

        // draw board
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    // light tile
                    g2d.setColor(new Color(237, 214, 176));
                } else {
                    // dark tile
                    g2d.setColor(new Color(184, 135, 98));
                }
                int squareX = x + col * Board.SQUARE_SIZE;
                int squareY = y + row * Board.SQUARE_SIZE;
                g2d.fillRect(squareX, squareY, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
            }
        }

        // draw selected piece
        if (this.selected != null) {
            // draw highlight
            Position highlight = new Position(this.selected);
            if (Board.player == Player.PLAYER2) {
                highlight.reflect();
            }
            int highlightX = x + highlight.getCol() * Board.SQUARE_SIZE;
            int highlightY = y + highlight.getRow() * Board.SQUARE_SIZE;
            g2d.setColor(new Color(255, 255, 0, 127));
            g2d.fillRect(highlightX, highlightY, Board.SQUARE_SIZE, Board.SQUARE_SIZE);

            // draw legal moves
            for (Move legalMove : this.getLegalMoves(this.selected)) {
                Position dest = legalMove.getDest();
                if (Board.player == Player.PLAYER2) {
                    dest.reflect();
                }
                int destX = x + dest.getCol() * Board.SQUARE_SIZE + Board.SQUARE_SIZE / 3;
                int destY = y + dest.getRow() * Board.SQUARE_SIZE + Board.SQUARE_SIZE / 3;
                legalMove.draw(g2d, destX, destY, Board.SQUARE_SIZE / 3, Board.SQUARE_SIZE / 3);
            }
        }

        // draw pieces
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (this.board[row][col] != null) {
                    Position piece = new Position(row, col);
                    if (Board.player == Player.PLAYER2) {
                        piece.reflect();
                    }
                    int pieceX = x + piece.getCol() * Board.SQUARE_SIZE + margin;
                    int pieceY = y + piece.getRow() * Board.SQUARE_SIZE + margin;
                    this.board[row][col].draw(g2d, pieceX, pieceY, 2 * Board.RADIUS, 2 * Board.RADIUS);
                }
            }
        }
    }

    private Piece getPiece(Position pos) {
        return this.board[pos.getRow()][pos.getCol()];
    }

    public boolean isPlayersTurn() {
        return this.playerTurn == Board.player;
    }

    public Move getPlayedMove() {
        return this.playedMove;
    }

    private void setPiece(Position pos, Piece piece) {
        this.board[pos.getRow()][pos.getCol()] = piece;
    }

    public void clearPlayedMove() {
        this.playedMove = null;
    }
}