# My Personal Project: Tetris

## Tetris Application

The application will be similar to the video game *Tetris*. It will have a playable version of the game where
differently shaped blocks fall onto a large grid. The goal of the game is to keep the blocks from touching the ceiling
by filling in full rows of blocks to clear them from the grid. The player will be able to rotate, horizontally move, 
accelerate downwards, or instantly drop the block. Some **features** will include saving the state of the game and
being able to load back that saved state at any time.

Anyone who likes the game Tetris but finds it a bit too challenging will enjoy the save and load aspect of this program.

I recently started playing Tetris a lot and have been trying to convince my friends to play as well. However, the game
does have a significant learning curve which discourages most people from spending very much time on it. Thus, having
the ability to save the game allows users to essentially undo their mistakes and replay certain states of the game in
order to practice quick decision-making without being discouraged by repeated losses.

## User Stories

- As a user, I want the blocks that I drop to remain on the board until they are cleared
- As a user, I want to be able to rotate the falling block clockwise and counterclockwise
- As a user, I want to be able to move the falling block left and right
- As a user, I want to be able to accelerate the falling block downward
- As a user, I want to be able to instantly drop the falling block
- As a user, I want to be able to have full lines of tiles automatically removed from the grid and added to my score
- As a user, I want to be able to view my current score
- As a user, I want to be able to save the state of a game
- As a user, I want to be able to load a saved game

## Controls
- Left Arrow - Move left
- Right Arrow - Move Right
- Down Arrow - Move down
- Up Arrow - Rotate Clockwise
- Z - Rotate Counterclockwise
- Space - Drop
- Escape - Pause game and open menu

## Instructions for Grader
1. Use the arrow keys to guide the block
2. You can generate the first required action related to adding Xs to a Y by
   pressing the Space key on your keyboard to drop the block. This will add 4 Tetrominos to the board field of the game.
3. You can generate the second required action related to adding Xs to a Y by
   continuing to drop blocks until you manage to fill in a full line on the board.
   This will remove all the Tetrominos in that row from the board field of the game.
4. You can locate my visual component by
   looking at the multicolored tiles that you dropped onto the board.
5. You can save the state of my application by pressing the Escape key on your keyboard,
   then clicking the save button in the popup menu.
6. You can reload the state of my application by pressing the Escape key on your keyboard,
   then clicking the load button in the popup menu.

## Phase 4: Task 2
Sat Apr 01 10:16:28 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:16:29 PDT 2023
LineShape dropped onto board.

Sat Apr 01 10:16:31 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:16:31 PDT 2023
1 line(s) cleared.

Sat Apr 01 10:16:33 PDT 2023
ZShape dropped onto board.

Sat Apr 01 10:16:35 PDT 2023
ZShape dropped onto board.

Sat Apr 01 10:16:37 PDT 2023
SquareShape dropped onto board.

Sat Apr 01 10:16:40 PDT 2023
ZShape dropped onto board.

Sat Apr 01 10:16:40 PDT 2023
1 line(s) cleared.

Sat Apr 01 10:16:42 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:16:43 PDT 2023
MirroredLShape dropped onto board.

Sat Apr 01 10:16:49 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:16:53 PDT 2023
TShape dropped onto board.

Sat Apr 01 10:16:57 PDT 2023
MirroredLShape dropped onto board.

Sat Apr 01 10:16:58 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:17:01 PDT 2023
MirroredLShape dropped onto board.

Sat Apr 01 10:17:02 PDT 2023
SquareShape dropped onto board.

Sat Apr 01 10:17:05 PDT 2023
ZShape dropped onto board.

Sat Apr 01 10:17:08 PDT 2023
SquareShape dropped onto board.

Sat Apr 01 10:17:09 PDT 2023
TShape dropped onto board.

Sat Apr 01 10:17:11 PDT 2023
MirroredLShape dropped onto board.

Sat Apr 01 10:17:12 PDT 2023
ZShape dropped onto board.

Sat Apr 01 10:17:14 PDT 2023
LineShape dropped onto board.

Sat Apr 01 10:17:14 PDT 2023
4 line(s) cleared.

Sat Apr 01 10:17:15 PDT 2023
ZShape dropped onto board.

Sat Apr 01 10:17:16 PDT 2023
TShape dropped onto board.

Sat Apr 01 10:17:16 PDT 2023
SquareShape dropped onto board.

Sat Apr 01 10:17:16 PDT 2023
LineShape dropped onto board.

Sat Apr 01 10:17:16 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:17:16 PDT 2023
LineShape dropped onto board.

Sat Apr 01 10:17:17 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:17:17 PDT 2023
LineShape dropped onto board.

Sat Apr 01 10:17:21 PDT 2023
MirroredLShape dropped onto board.

Sat Apr 01 10:17:22 PDT 2023
LineShape dropped onto board.

Sat Apr 01 10:17:22 PDT 2023
2 line(s) cleared.

Sat Apr 01 10:17:23 PDT 2023
ZShape dropped onto board.

Sat Apr 01 10:17:23 PDT 2023
LShape dropped onto board.

Sat Apr 01 10:17:23 PDT 2023
SquareShape dropped onto board.

Sat Apr 01 10:17:23 PDT 2023
Game ended with 8 line(s) cleared.

## Phase 4: Task 3
Looking at my UML diagram, there are a few associations that could be removed. In particular, both Tetris and GamePanel
have an association with Game even though Tetris already has an association with GamePanel. Since the intention is for
both classes to have access to the same instance of Game, I could implement the observer pattern so that GamePanel can
update itself whenever changes in Game occur without needing to have an association with it. In fact, it would be
beneficial to change most of the actions in the Game class to send signals to other observers. Although I have already
implemented the observer pattern between Game and ScorePanel, I used the deprecated Observer class that is built into
Java. I would like to design my own Observer and Observable classes in order to have more control over the updating
behaviour of my panels.
Further, this would allow me to optimize one aspect of my program, which is that the program redraws the entire board
every tick. This is an expensive task and could be optimized to only redraw parts of the board that are changed.
Signalling what part of the board has changed and when it has changed is much easier with the observer pattern. This
could also be extended to future additional classes, such as the upcoming blocks panel, the held block panel, or the
statistics tracker. In this way, implementing the observer pattern would make implementing current features and
future features much more manageable and efficient while maintaining low coupling.