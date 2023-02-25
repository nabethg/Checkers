# Checkers Game

## Purpose
Our project is a Checkers game with a modern graphical user interface. The objective of checkers is to capture all opponent pieces before the player's time limit ends. The game features a PvP and a PvAI game mode while incorporating custom settings, player profiles, a rating system, and a help menu.

## GUI Structure
The GUI of this program is meant to be simplistic and modern, featuring a flat look and feel with high-quality images used for the checker pieces, board, and the window icon, which is present in all windows. Additionally, the game window displays the two playing parties' profiles, containing their avatar, username, and rating, in addition to a timer.

## Game Input
The user can play the game using a mouse. If a piece is clicked on, its position will be highlighted on the board and its legal moves will be displayed. At this stage, the user can either click on any legal move displayed to perform it, select an alternate piece, or reset their selection by clicking on empty space on the game board. In addition to mouse input, to interact with the menu bar, the user can use the set shortcuts for the relevant menu items or the mnemonic of each menu.

## Settings
The user can choose between a native, dark, or light-themed GUI through the settings window. They can also enable audio feedback from the game. For the AI difficulty, the user can choose an intelligence level from 1 to 8. Moreover, the user can select the player time limit from a standard selection of times ranging from 1 to 30 minutes. The user can also choose to play as black or red, with black playing first. When the user is done modifying the settings, they can either hit apply to save the changes for future games or cancel to discard any modifications made.
## Profile
The user can change their username in the profile window by typing into the text field and hitting Apply to save the changes. Their read-only rating is also visible. Additionally, the user can press the edit button below their avatar to update it with a png image chosen from their computer.

## Play
The user can play using the three options available in the play menu.

When the user hosts a PvP game, their opponent can join their game by entering the host's IP address, and a new game with the host's game settings will commence. During this time, the two players also transfer their profile information via the local network.

In addition to PvP, the user has the option to play against an AI locally. The AI has a friendly profile with a rating corresponding to its difficulty level.

## Additional Menu Items
Lastly, the menu bar also contains a help menu with a simple tutorial on how to play, official rules, and relevant information about this program. Furthermore, in addition to the windows buttons, the user can quit the program through the file menu.