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
 * The {@code Visualizer} class displays a frame on the screen containing the
 * graphical representation of a checkers game.
 */
public class Visualizer {
    private JFrame frame;

    public Visualizer(Checkers game) {
        // theme
        try {
            switch (game.getPlayerSettings().getTheme()) {
                case Settings.NATIVE_THEME:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case Settings.DARK_THEME:
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case Settings.LIGHT_THEME:
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
            }
        } catch (Exception e) {
            this.showMessage("Error", "Failed to set theme!", JOptionPane.ERROR_MESSAGE);
        }
        this.frame = new JFrame("Checkers");

        // icon image
        ImageIcon icon = new ImageIcon(
                this.getClass().getResource(Resources.ICON));
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