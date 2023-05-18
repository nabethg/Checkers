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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The {@code ProfileFrame} class displays a frame on the screen containing
 * the profile editor, allowing the user to modify their username and avatar
 * image.
 */
public class ProfileFrame extends JFrame {
    private Checkers game;
    private JTextField usernameTextField;
    private JButton editButton, applyButton, cancelButton;

    public ProfileFrame(Checkers game) {
        super("Profile");
        this.game = game;

        // icon image
        ImageIcon icon = new ImageIcon(this.getClass().getResource(Resources.ICON));
        this.setIconImage(icon.getImage());

        // panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(300, 185));
        this.add(panel);

        // username
        JLabel usernameLabel = new JLabel("Username");
        this.usernameTextField = new JTextField(this.game.getPlayerProfile().getUsername());
        this.usernameTextField.setPreferredSize(new Dimension(150, this.usernameTextField.getPreferredSize().height));
        panel.add(usernameLabel);
        panel.add(this.usernameTextField);

        // rating
        JLabel ratingLabel = new JLabel("<html>Rating<br/>" + this.game.getPlayerProfile().getRating() + "</html>");
        panel.add(ratingLabel);

        // avatar
        JLabel avatar = new JLabel();
        avatar.setIcon(
                new ImageIcon(this.game.getPlayerProfile().getAvatar().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        panel.add(avatar);

        // buttons
        this.editButton = new JButton("Edit");
        this.applyButton = new JButton("Apply");
        this.cancelButton = new JButton("Cancel");
        this.editButton.setPreferredSize(new Dimension(60, this.editButton.getPreferredSize().height));
        this.applyButton.setPreferredSize(new Dimension(90, this.applyButton.getPreferredSize().height));
        this.cancelButton.setPreferredSize(new Dimension(90, this.cancelButton.getPreferredSize().height));
        this.editButton.setMargin(new Insets(0, 0, 0, 0));
        this.applyButton.setMargin(new Insets(0, 0, 0, 0));
        this.cancelButton.setMargin(new Insets(0, 0, 0, 0));
        panel.add(this.editButton);
        panel.add(this.applyButton);
        panel.add(this.cancelButton);

        // event listener
        EventHandler listener = new EventHandler();
        this.editButton.addActionListener(listener);
        this.applyButton.addActionListener(listener);
        this.cancelButton.addActionListener(listener);

        // size and location of components
        usernameLabel.setBounds(new Rectangle(new Point(120, 15), usernameLabel.getPreferredSize()));
        this.usernameTextField.setBounds(new Rectangle(new Point(120, 35), this.usernameTextField.getPreferredSize()));
        ratingLabel.setBounds(new Rectangle(new Point(120, 70), ratingLabel.getPreferredSize()));
        avatar.setBounds(new Rectangle(new Point(30, 15), avatar.getPreferredSize()));
        this.editButton.setBounds(new Rectangle(new Point(30, 80), this.editButton.getPreferredSize()));
        this.applyButton.setBounds(new Rectangle(new Point(52, 135), this.applyButton.getPreferredSize()));
        this.cancelButton.setBounds(new Rectangle(new Point(158, 135), this.cancelButton.getPreferredSize()));

        // frame
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // inner class
    private class EventHandler implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (ev.getSource() == ProfileFrame.this.cancelButton) {
                dispose();
            } else if (ev.getSource() == ProfileFrame.this.applyButton) {
                try {
                    // update username
                    ProfileFrame.this.game.getPlayerProfile()
                            .setUsername(ProfileFrame.this.usernameTextField.getText());
                    ProfileFrame.this.game.getPlayerProfile().writeToTxt(Resources.PATH + Resources.PLAYER_PROFILE);

                    dispose();
                } catch (FileNotFoundException e) {
                    Visualizer.showMessage(ProfileFrame.this, "Error", "Profile File Not Found!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (ev.getSource() == ProfileFrame.this.editButton) {
                try {
                    // choose png
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
                    if (fileChooser.showOpenDialog(ProfileFrame.this) == JFileChooser.APPROVE_OPTION) {
                        Image temp = ImageIO.read(fileChooser.getSelectedFile()).getScaledInstance(
                                CheckersPanel.ICON_SIZE, CheckersPanel.ICON_SIZE, Image.SCALE_SMOOTH);

                        // scale avatar
                        BufferedImage newAvatar = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
                                BufferedImage.TYPE_INT_ARGB);
                        newAvatar.getGraphics().drawImage(temp, 0, 0, null);

                        // update avatar
                        ProfileFrame.this.game.getPlayerProfile().setAvatar(newAvatar);
                        ImageIO.write(newAvatar, "png", new File(Resources.PATH + Resources.PLAYER_AVATAR));

                        dispose();
                        new ProfileFrame(ProfileFrame.this.game);
                    }
                } catch (IOException e) {
                    Visualizer.showMessage(ProfileFrame.this, "Error", "Failed to change avatar image!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}