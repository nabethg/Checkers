import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

/**
 * [GameRunner.java]
 * A class that maintains the flow of a checkers game based on the player turn.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class GameRunner extends Thread {
    Checkers game;

    public GameRunner(Checkers game) {
        this.game = game;
    }

    private void transferProfiles() throws IOException, ClassNotFoundException {
        this.game.getClient().write(this.game.getPlayerProfile());
        this.game.setOppProfile(this.game.getClient().readProfile());
    }

    private void playSound(String fileName) {
        if (this.game.getPlayerSettings().hasSoundEffects()) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        URL inputStream = getClass().getResource(fileName);
                        AudioInputStream input = AudioSystem.getAudioInputStream(inputStream);

                        Clip clip = AudioSystem.getClip();
                        clip.open(input);
                        clip.start();
                    } catch (Exception e) {
                        GameRunner.this.game.getVisualizer().showMessage("Error", "Failed to play sound effect!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.start();
        }
    }

    private void sendMove() throws IOException {
        // receive move from user
        while (this.game.getBoard().getPlayedMove() == null) {
            Thread.yield();
        }
        // send move
        if (this.game.isPvP() == true) {
            this.game.getClient().write(this.game.getBoard().getPlayedMove());
        }
        // perform move
        if (this.game.getBoard().getPlayedMove().isCapturing()) {
            this.playSound(Resources.CAPTURING_MOVE);
        } else {
            this.playSound(Resources.ORDINARY_MOVE);
        }
        this.game.getBoard().perform(game.getBoard().getPlayedMove());
        this.game.getBoard().clearPlayedMove();
    }

    private void receiveMove() throws IOException, ClassNotFoundException {
        // receive move from opponent
        Move move;
        if (this.game.isPvP() == true) {
            move = this.game.getClient().readMove();
        } else {
            move = this.game.getAIPlayer().decide(this.game.getBoard());
        }
        // perform move
        if (move.isCapturing()) {
            this.playSound(Resources.CAPTURING_MOVE);
        } else {
            this.playSound(Resources.ORDINARY_MOVE);
        }
        this.game.getBoard().perform(move);
    }

    public void endGame() {
        new Thread() {
            @Override
            public void run() {
                try {
                    GameRunner.this.playSound(Resources.GAME_OVER);

                    GameRunner.this.game.resetGame();

                    // get winner
                    Player winner = GameRunner.this.game.getBoard().getWinner();
                    if (GameRunner.this.game.getPlayerTimer().getTimeLeft() <= 0) {
                        winner = GameRunner.this.game.getPlayer().getOpponent();
                    } else if (GameRunner.this.game.getOppTimer().getTimeLeft() <= 0) {
                        winner = GameRunner.this.game.getPlayer();
                    }
                    // update PvP ratings
                    if (GameRunner.this.game.isPvP()) {
                        boolean playerWon = GameRunner.this.game.getPlayer() == winner;
                        GameRunner.this.game.getPlayerProfile()
                                .updateRating(GameRunner.this.game.getOppProfile().getRating(), playerWon);
                        GameRunner.this.game.getPlayerProfile().writeToTxt(Resources.PLAYER_PROFILE);
                    }

                    GameRunner.this.game.getVisualizer().showMessage("Game Over", winner.toString() + " won the game!",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    GameRunner.this.game.showIOExceptionMessage();
                }
            }
        }.start();
    }

    @Override
    public void run() {
        try {
            if (this.game.isPvP()) {
                this.transferProfiles();
            }

            this.playSound(Resources.GAME_START);

            this.game.getPlayerTimer().start();
            this.game.getOppTimer().start();
            do {
                if (this.game.getBoard().isPlayersTurn()) {
                    this.game.getPlayerTimer().unpause();
                    this.sendMove();
                    this.game.getPlayerTimer().pause();
                } else {
                    this.game.getOppTimer().unpause();
                    this.receiveMove();
                    this.game.getOppTimer().pause();
                }
            } while (!this.game.getBoard().gameIsOver());
            this.endGame();
        } catch (IOException e) {
            this.game.showIOExceptionMessage();
        } catch (ClassNotFoundException e) {
            this.game.showClassNotFoundExceptionMessage();
        }
    }
}