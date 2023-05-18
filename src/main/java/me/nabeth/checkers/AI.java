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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * The {@code AI} class encapsulates a checkers AI player the format of its
 * profile, depth, and decision making algorithm.
 */
public class AI {
    private Profile profile;
    private int depth;

    public AI(int depth) throws IOException {
        this.depth = depth;
        String username = "Pepe the Frog";
        int rating = 300 * depth;
        BufferedImage avatar = ImageIO.read(this.getClass().getResource(Resources.AI_AVATAR));
        this.profile = new Profile(username, rating, avatar);
    }

    /**
     * Returns the evaluation of the board based on the player turn and its
     * children.
     * 
     * @param board the board state being searched
     * @param depth the remaining number of levels in the search tree
     * @param alpha the evaluation of the board resulted from the best move by the
     *              AI
     * @param beta  the evaluation of the board resulted from the best move by the
     *              opponent
     * @return a more positive {@code int} if the evaluation of the board is
     *         favorable towards the AI, otherwise a more negative {@code int} if
     *         the evaluation of the board is favorable towards the opponent
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
     * Returns the most favorable move the AI can play based on a minimax algorithm
     * and the specified depth.
     * 
     * @param board the board state that the AI has to perform a move on
     * @return a {@code Move} to be performed
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