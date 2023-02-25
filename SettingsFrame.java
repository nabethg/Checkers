import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * [SettingsFrame.java]
 * A class that displays a frame on the screen containing the settings editor,
 * allowing the user to modify all settings parameters.
 * 
 * @author Nabeth Ghazi
 * @author Edwin Sun
 * @version June 2022
 */
public class SettingsFrame extends JFrame {
    private Checkers game;
    private JButton applyButton, cancelButton;
    private JComboBox<String> themeComboBox;
    private JComboBox<String> soundEffectsComboBox;
    private JComboBox<String> AIDifficultyComboBox;
    private JComboBox<String> timeLimitComboBox;
    private JComboBox<String> playAsComboBox;

    public SettingsFrame(Checkers game) {
        super("Settings");
        this.game = game;

        // icon image
        ImageIcon icon = new ImageIcon(Resources.ICON);
        this.setIconImage(icon.getImage());

        // theme
        JLabel themeLabel = new JLabel("GUI Theme");
        this.themeComboBox = new JComboBox<String>();
        this.themeComboBox.addItem("Native Theme");
        this.themeComboBox.addItem("Dark Theme");
        this.themeComboBox.addItem("Light Theme");
        this.themeComboBox.setSelectedIndex(game.getPlayerSettings().getTheme());
        this.add(themeLabel);
        this.add(this.themeComboBox);

        // sound effects
        JLabel soundEffectsLabel = new JLabel("Sound Effects");
        this.soundEffectsComboBox = new JComboBox<String>();
        this.soundEffectsComboBox.addItem("On");
        this.soundEffectsComboBox.addItem("Off");
        if (!this.game.getPlayerSettings().hasSoundEffects()) {
            this.soundEffectsComboBox.setSelectedIndex(1);
        }
        this.add(soundEffectsLabel);
        this.add(this.soundEffectsComboBox);

        // AI difficulty
        JLabel AIDifficultyLabel = new JLabel("AI Difficulty");
        this.AIDifficultyComboBox = new JComboBox<String>();
        for (int difficulty = Settings.MIN_AI_DIFFICULTY; difficulty <= Settings.MAX_AI_DIFFICULTY; difficulty++) {
            this.AIDifficultyComboBox.addItem(Integer.toString(difficulty));
        }
        this.AIDifficultyComboBox.setSelectedIndex(this.game.getPlayerSettings().getAIDifficulty() - 1);
        this.add(AIDifficultyLabel);
        this.add(this.AIDifficultyComboBox);

        // time limit
        JLabel timeLimitLabel = new JLabel("Time Limit");
        this.timeLimitComboBox = new JComboBox<String>();
        for (int index = 0; index < Settings.TIME_LIMITS.length; index++) {
            if (Settings.TIME_LIMITS[index] == 1) {
                this.timeLimitComboBox.addItem(Settings.TIME_LIMITS[index] + " minute");
            } else {
                this.timeLimitComboBox.addItem(Settings.TIME_LIMITS[index] + " minutes");
            }
        }
        this.timeLimitComboBox.setSelectedIndex(game.getPlayerSettings().getTimeLimitIndex());
        this.add(timeLimitLabel);
        this.add(this.timeLimitComboBox);

        // goes first
        JLabel playAsLabel = new JLabel("Play As");
        this.playAsComboBox = new JComboBox<String>();
        this.playAsComboBox.addItem("Black");
        this.playAsComboBox.addItem("Red");
        if (!this.game.getPlayerSettings().PlayAsBlack()) {
            this.playAsComboBox.setSelectedIndex(1);
        }
        this.add(playAsLabel);
        this.add(this.playAsComboBox);

        // buttons
        this.applyButton = new JButton("Apply");
        this.cancelButton = new JButton("Cancel");
        this.add(this.applyButton);
        this.add(this.cancelButton);

        // event listener
        EventHandler listener = new EventHandler();
        this.applyButton.addActionListener(listener);
        this.cancelButton.addActionListener(listener);

        // size and location of components
        themeLabel.setBounds(25, 25, 200, 25);
        this.themeComboBox.setBounds(195, 25, 200, 25);
        soundEffectsLabel.setBounds(25, 60, 200, 25);
        this.soundEffectsComboBox.setBounds(195, 60, 200, 25);
        AIDifficultyLabel.setBounds(25, 95, 200, 25);
        this.AIDifficultyComboBox.setBounds(195, 95, 200, 25);
        timeLimitLabel.setBounds(25, 130, 200, 25);
        this.timeLimitComboBox.setBounds(195, 130, 200, 25);
        playAsLabel.setBounds(25, 165, 200, 25);
        this.playAsComboBox.setBounds(195, 165, 200, 25);
        this.applyButton.setBounds(110, 220, 100, 30);
        this.cancelButton.setBounds(220, 220, 100, 30);

        // frame
        this.setSize(new Dimension(450, 330));
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private class EventHandler implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (ev.getSource() == SettingsFrame.this.cancelButton) {
                dispose();
            } else if (ev.getSource() == SettingsFrame.this.applyButton) {
                try {
                    Settings settings = SettingsFrame.this.game.getPlayerSettings();
                    int prevTheme = settings.getTheme();

                    // update settings
                    settings.setTheme(SettingsFrame.this.themeComboBox.getSelectedIndex());
                    settings.setHasSoundEffects(SettingsFrame.this.soundEffectsComboBox.getSelectedIndex() == 0);
                    settings.setAIDifficulty(SettingsFrame.this.AIDifficultyComboBox.getSelectedIndex() + 1);
                    settings.setTimeLimitIndex(SettingsFrame.this.timeLimitComboBox.getSelectedIndex());
                    settings.setPlayAsBlack(SettingsFrame.this.playAsComboBox.getSelectedIndex() == 0);
                    settings.writeToTxt(Resources.PLAYER_SETTINGS);

                    // update game
                    if (prevTheme != settings.getTheme()) {
                        SettingsFrame.this.game.getVisualizer().dispose();
                        SettingsFrame.this.game.setVisualizer(new Visualizer(SettingsFrame.this.game));
                    }
                    SettingsFrame.this.game.setAIPlayer(new AI(settings.getAIDifficulty()));
                    if (!SettingsFrame.this.game.inGame()) {
                        SettingsFrame.this.game.setGameParams(settings);
                    }

                    dispose();
                } catch (FileNotFoundException e) {
                    Visualizer.showMessage(SettingsFrame.this, "Error", "Settings File Not Found!",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    SettingsFrame.this.game.showIOExceptionMessage();
                }
            }
        }
    }
}