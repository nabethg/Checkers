import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 * [CheckersPanel.java]
 * A class that draws a checkers game to a panel to be displayed on a frame
 * while handling mouse input.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class CheckersPanel extends JPanel implements MouseInputListener {
    public static final int ICON_SIZE = 60;
    private static final int MARGIN = 15;
    private static final int WIDTH = 2 * MARGIN + Board.SIZE * Board.SQUARE_SIZE;
    private static final int HEIGHT = 4 * MARGIN + 2 * ICON_SIZE + Board.SIZE * Board.SQUARE_SIZE;

    private static Color txtColor;
    private static Color shapeColor;
    private Checkers game;

    public CheckersPanel(Checkers game) {
        if (game.getPlayerSettings().getTheme() == Settings.DARK_THEME) {
            CheckersPanel.txtColor = Color.LIGHT_GRAY;
            CheckersPanel.shapeColor = new Color(28, 28, 28);
        } else {
            CheckersPanel.txtColor = Color.DARK_GRAY;
            CheckersPanel.shapeColor = new Color(170, 170, 170);
            this.setBackground(new Color(217, 217, 217));
        }
        this.game = game;
        this.setPreferredSize(new Dimension(CheckersPanel.WIDTH, CheckersPanel.HEIGHT));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        // draw board
        this.game.getBoard().draw(g2d, CheckersPanel.MARGIN, 2 * CheckersPanel.MARGIN + CheckersPanel.ICON_SIZE,
                Board.SIZE * Board.SQUARE_SIZE, Board.SIZE * Board.SQUARE_SIZE);

        // draw profiles
        this.game.getPlayerProfile().draw(g2d, CheckersPanel.MARGIN,
                CheckersPanel.HEIGHT - CheckersPanel.MARGIN - CheckersPanel.ICON_SIZE, CheckersPanel.ICON_SIZE,
                CheckersPanel.ICON_SIZE);
        try {
            this.game.getOppProfile().draw(g2d, CheckersPanel.MARGIN, CheckersPanel.MARGIN, CheckersPanel.ICON_SIZE,
                    CheckersPanel.ICON_SIZE);
        } catch (IOException e) {
            this.game.showIOExceptionMessage();
        }

        // draw timers
        int timerX = CheckersPanel.WIDTH - (int) (1.5 * Board.SQUARE_SIZE) - CheckersPanel.MARGIN;
        int timerWidth = (int) (1.5 * Board.SQUARE_SIZE);
        this.game.getPlayerTimer().draw(g2d, timerX,
                CheckersPanel.HEIGHT - CheckersPanel.ICON_SIZE - CheckersPanel.MARGIN,
                timerWidth, CheckersPanel.ICON_SIZE);
        this.game.getOppTimer().draw(g2d, timerX, CheckersPanel.MARGIN, timerWidth, CheckersPanel.ICON_SIZE);

        this.repaint();
    }

    public static Color getTxtColor() {
        return CheckersPanel.txtColor;
    }

    public static Color getShapeColor() {
        return CheckersPanel.shapeColor;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // translate click to origin of the board
        int mouseX = e.getX() - CheckersPanel.MARGIN;
        int mouseY = e.getY() - (2 * CheckersPanel.MARGIN + CheckersPanel.ICON_SIZE);
        if (this.game.getPlayer() == Player.PLAYER2) {
            mouseX = Board.SQUARE_SIZE * Board.SIZE - mouseX;
            mouseY = Board.SQUARE_SIZE * Board.SIZE - mouseY;
        }

        if ((this.game.getBoard().isPlayersTurn()) && (this.game.inGame())) {
            if (!this.game.getBoard().select(mouseX, mouseY)) { // a position without a piece was clicked
                this.game.getBoard().validateMove(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}