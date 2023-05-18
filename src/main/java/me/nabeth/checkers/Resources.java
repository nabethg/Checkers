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

/**
 * The {@code Resources} final class contains a collection of constant values
 * representing the directories of all resource files used throughout the
 * {@code me.nabeth.checkers} package.
 */
public final class Resources {
    public static final String PATH = "src/main/resources";
    // Piece
    public static final String PLAYER1_PAWN = "/Piece/BLACK_PAWN.png";
    public static final String PLAYER1_KING = "/Piece/BLACK_KING.png";
    public static final String PLAYER2_PAWN = "/Piece/RED_PAWN.png";
    public static final String PLAYER2_KING = "/Piece/RED_KING.png";
    // Profile
    public static final String DEFAULT_AVATAR = "/Profile/DEFAULT_AVATAR.png";
    public static final String PLAYER_AVATAR = "/Profile/PLAYER_AVATAR.png";
    public static final String AI_AVATAR = "/Profile/AI_AVATAR.png";
    public static final String PLAYER_PROFILE = "/Profile/PLAYER_PROFILE.txt";
    // Settings
    public static final String PLAYER_SETTINGS = "/Settings/PLAYER_SETTINGS.txt";
    // Sounds
    public static final String GAME_START = "/Sounds/GAME_START.wav";
    public static final String ORDINARY_MOVE = "/Sounds/ORDINARY_MOVE.wav";
    public static final String CAPTURING_MOVE = "/Sounds/CAPTURING_MOVE.wav";
    public static final String GAME_OVER = "/Sounds/GAME_OVER.wav";
    // Visualizer
    public static final String ICON = "/Visualizer/ICON.png";

    private Resources() {
    }
}