import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * [AI.java]
 * A class that encapsulates a checkers AI player the format of its profile,
 * depth, and decision making algorithm.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class AI {
    private Profile profile;
    private int depth;

    public AI(int depth) throws IOException {
        this.depth = depth;
        String username = "Pepe the Frog";
        int rating = 300 * depth;
        BufferedImage avatar = ImageIO.read(new File(Resources.AI_AVATAR));
        this.profile = new Profile(username, rating, avatar);
    }

    /**
     * minimax
     * Returns the evaluation of the board based on the player
     * turn and its children.
     * 
     * @param board a Board representing the board state being searched.
     * @param depth an int representing the remaining number of levels in the search
     *              tree.
     * @param alpha an int representing the evaluation of the board resulted from
     *              the best move by the AI.
     * @param beta  an int representing the evaluation of the board resulted from
     *              the best move by the opponent.
     * @return a more positive int if the evaluation of the board is favorable
     *         towards the AI, otherwise a more negative int if the evaluation of
     *         the board is favorable towards the opponent.
     */
    private int minimax(Board board, int depth, int alpha, int beta) {
        if ((depth == 0) || (board.gameIsOver())) {
            return board.heuristic();
        }

        if (board.isPlayersTurn()) {
            int minEval = Integer.MAX_VALUE;
            for (Move legalMove : board.getLegalMoves()) {
                Board child = new Board(board);
                child.perform(legalMove);
                int eval = minimax(child, depth - 1, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) { // prune the search
                    break;
                }
            }
            return minEval;

        } else {
            int maxEval = Integer.MIN_VALUE;
            for (Move legalMove : board.getLegalMoves()) {
                Board child = new Board(board);
                child.perform(legalMove);
                int eval = minimax(child, depth - 1, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                beta = Math.max(beta, eval);
                if (beta <= alpha) { // prune the search
                    break;
                }
            }
            return maxEval;
        }
    }

    /**
     * decide
     * Returns the most favorable move the AI can play based on a minimax algorithm
     * and the specified depth.
     * 
     * @param board a Board representing the board state that the AI has to perform
     *              a move on.
     * @return a Move to be performed.
     */
    public Move decide(Board board) {
        int maxEval = Integer.MIN_VALUE;
        Move move = null;
        Set<Move> legalMoves = board.getLegalMoves();
        if (legalMoves.size() == 1) { // no searching needed
            return legalMoves.iterator().next();
        }
        for (Move legalMove : legalMoves) {
            Board child = new Board(board);
            child.perform(legalMove);
            int eval = minimax(child, this.depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (eval > maxEval) {
                move = legalMove;
                maxEval = eval;
            }
        }
        return move;
    }

    public Profile getProfile() {
        return this.profile;
    }
}