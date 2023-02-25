import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * [Visualizer.java]
 * A class that displays a frame on the screen containing the graphical
 * representation of a checkers game.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class Visualizer {
    private JFrame frame;

    public Visualizer(Checkers game) {
        // theme
        try {
            if (game.getPlayerSettings().getTheme() == Settings.NATIVE_THEME) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else if (game.getPlayerSettings().getTheme() == Settings.DARK_THEME) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else if (game.getPlayerSettings().getTheme() == Settings.LIGHT_THEME) {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (Exception e) {
            this.showMessage("Error", "Failed to set theme!", JOptionPane.ERROR_MESSAGE);
        }
        this.frame = new JFrame("Checkers");

        // icon image
        ImageIcon icon = new ImageIcon(Resources.ICON);
        this.frame.setIconImage(icon.getImage());

        // panel
        JPanel gamePanel = new CheckersPanel(game);
        this.frame.add(gamePanel);

        // menu bar
        JMenuBar menuBar = new CheckersMenuBar(this.frame, game);
        this.frame.setJMenuBar(menuBar);

        // window listener
        WindowListener listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        };
        this.frame.addWindowListener(listener);

        // frame
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public static void showMessage(JFrame frame, String title, String message, int messageType) {
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }

    public void showMessage(String title, String message, int messageType) {
        Visualizer.showMessage(this.frame, title, message, messageType);
    }

    public String showInputPrompt(String title, String message) {
        return JOptionPane.showInputDialog(this.frame, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public void dispose() {
        this.frame.dispose();
    }
}