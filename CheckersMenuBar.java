import java.awt.Desktop;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * [CheckersMenuBar.java]
 * A class that organizes functionality of the program as a menubar to be
 * displayed on a frame.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class CheckersMenuBar extends JMenuBar {
    public CheckersMenuBar(JFrame frame, Checkers game) {
        // file menu
        JMenu fileMenu = new JMenu("File");
        this.add(fileMenu);
        fileMenu.setMnemonic('F');

        // settings item
        JMenuItem settingsItem = new JMenuItem("Settings");
        fileMenu.add(settingsItem);
        settingsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SettingsFrame(game);
            }
        });
        settingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));

        // profile item
        JMenuItem profileItem = new JMenuItem("Profile");
        fileMenu.add(profileItem);
        profileItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProfileFrame(game);
            }
        });
        profileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));

        // exit item
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit",
                        JOptionPane.YES_NO_OPTION) == 0) {
                    System.exit(0);
                }
            }
        });

        // play menu
        JMenu playMenu = new JMenu("Play");
        this.add(playMenu);
        playMenu.setMnemonic('P');

        // host PvP item
        JMenuItem hostPvPItem = new JMenuItem("Host PvP Game");
        playMenu.add(hostPvPItem);
        hostPvPItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.hostPvPGame();
            }
        });
        hostPvPItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));

        // join PvP item
        JMenuItem joinPvPItem = new JMenuItem("Join PvP Game");
        playMenu.add(joinPvPItem);
        joinPvPItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.joinPvPGame();
            }
        });
        joinPvPItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, Event.CTRL_MASK));

        // new PvAI item
        JMenuItem PvAIItem = new JMenuItem("New PvAI Game");
        playMenu.add(PvAIItem);
        PvAIItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.newPvAIGame();
            }
        });
        PvAIItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));

        // help menu
        JMenu helpMenu = new JMenu("Help");
        this.add(helpMenu);
        helpMenu.setMnemonic('H');

        // how to play item
        JMenuItem howToPlayItem = new JMenuItem("How To Play");
        helpMenu.add(howToPlayItem);
        howToPlayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Click on a piece and click on a highlighted square to perform a legal move.",
                        "How To Play", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // official rules
        JMenuItem officialRulesItem = new JMenuItem("Official Rules");
        helpMenu.add(officialRulesItem);
        officialRulesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop desktop = java.awt.Desktop.getDesktop();
                    URI oURL = new URI("https://www.wcdf.net/rules/rules_of_checkers_english.pdf");
                    desktop.browse(oURL);
                } catch (Exception ex) {
                    Visualizer.showMessage(frame, "Error", "Failed to load official rules!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        officialRulesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

        // about item
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "Checkers (Draughts) game created by NG and ES.\nVersion: " + Checkers.VERSION, "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}