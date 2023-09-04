package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 * Represents the whole tetris game
 */
public class Game extends Observable implements Writable {
    public static final int TICKS_PER_FALL = 50;
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    public static final int WIDTH = (BOARD_WIDTH - 1) * Tetromino.SIZE;
    public static final int HEIGHT = BOARD_HEIGHT * Tetromino.SIZE;

    public static final String EVENT_LINE_CLEARED = "LINE CLEARED";

    private boolean ended = false;
    private int score = 0;
    private Block curBlock = new Block();
    private Tetromino[][] board;
    private int ticksUntilFall = TICKS_PER_FALL;

    // EFFECT: initializes a game with an empty board of size maxX,maxY
    public Game() {
        board = new Tetromino[BOARD_WIDTH][BOARD_HEIGHT];
        clearBoard();
    }

    // MODIFIES: this
    // EFFECT: updates the game each tick by trying to move and fall the block,
    //         if the block is dropped, a new block is created and the game over condition is checked
    public void tick() {
        if (!ended) {
            if (curBlock.getNextMove() == Moves.Drop) {
                drop();
            } else if (tryMove(curBlock.getNextMove())) {
                if (curBlock.getNextMove() == Moves.Down) {
                    resetFall();
                }
                curBlock.move();
            } else {
                curBlock.setNextMove(Moves.None);
            }

            if (ticksUntilFall == 0) {
                if (tryFall()) {
                    curBlock.fall();
                } else {
                    checkFullLines();
                    curBlock = new Block();
                    checkGameOver();
                }
                resetFall();
            }

            ticksUntilFall--;
        }
    }

    // Responds to key press codes
    // MODIFIES: this
    // EFFECTS: sets the block's next move according to the given key code
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_RIGHT:
                curBlock.setNextMove(Moves.Right);
                break;
            case KeyEvent.VK_LEFT:
                curBlock.setNextMove(Moves.Left);
                break;
            case KeyEvent.VK_DOWN:
                curBlock.setNextMove(Moves.Down);
                break;
            case KeyEvent.VK_UP:
                curBlock.setNextMove(Moves.CW);
                break;
            case KeyEvent.VK_Z:
                curBlock.setNextMove(Moves.CCW);
                break;
            case KeyEvent.VK_SPACE:
                curBlock.setNextMove(Moves.Drop);
                break;
            default:
                curBlock.setNextMove(Moves.None);
        }
    }

    // EFFECT: returns true if the position that a block will move to is within the bounds
    //         and not already occupied, otherwise returns false
    private boolean tryMove(Moves nextMove) {
        boolean isValid = true;
        for (int i = 0; i < 4; i++) {
            int x = curBlock.getRelativePos()[i].getX();
            int y = curBlock.getRelativePos()[i].getY();
            Position newPos = predictMove(x, y, nextMove);
            newPos.setX(newPos.getX() + curBlock.getCentrePosOnBoard().getX());
            newPos.setY(newPos.getY() + curBlock.getCentrePosOnBoard().getY());

            if (newPos.getX() >= BOARD_WIDTH || newPos.getY() >= BOARD_HEIGHT
                    || newPos.getX() < 0 || board[newPos.getX()][newPos.getY()] != Tetromino.Empty) {
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    // EFFECT: returns the position that a tile will be at after it moves
    private Position predictMove(int x, int y, Moves nextMove) {
        Position newPos = new Position(x, y);
        switch (nextMove) {
            case Right:
                newPos.setX(x + 1);
                break;
            case Left:
                newPos.setX(x - 1);
                break;
            case Down:
                newPos.setY(y + 1);
                break;
            case CW:
            case CCW:
                newPos = predictRotate(x, y, nextMove);
                break;
        }

        return newPos;
    }

    // MODIFIES: this
    // EFFECT: returns the position that a tile will be at after it rotates
    private Position predictRotate(int x, int y, Moves nextMove) {
        Position newPos = new Position(x, y);
        if (nextMove == Moves.CW) {
            newPos.setX(y * -1);
            newPos.setY(x);
        } else {
            newPos.setY(x * -1);
            newPos.setX(y);
        }

        return newPos;
    }


    // EFFECT: returns true if the position that a block will fall to is within the bounds
    //         and not already occupied, otherwise drops the block and returns false
    private boolean tryFall() {
        boolean canFall = false;
        for (Position pos : curBlock.getAllPosOnBoard()) {
            int newY = pos.getY() + 1;

            if (newY >= BOARD_HEIGHT || board[pos.getX()][newY] != Tetromino.Empty) {
                blockDropped();
                canFall = false;
                break;
            } else {
                canFall = true;
            }
        }

        return canFall;
    }

    // MODIFIES: this
    // EFFECT: falls the block until it is dropped
    private void drop() {
        while (tryMove(Moves.Down)) {
            curBlock.fall();
        }

        ticksUntilFall = 0;
    }

    // MODIFIES: this
    // EFFECT: copies each tile from the block onto the board
    private void blockDropped() {
        for (Position pos : curBlock.getAllPosOnBoard()) {
            board[pos.getX()][pos.getY()] = curBlock.getShape();
        }

        EventLog.getInstance().logEvent(new Event(curBlock.getShape().getLabel() + " dropped onto board."));
    }

    // MODIFIES: this
    // EFFECT: clears all full rows from the board and moves the above
    //         tiles down to fill in the gap, then increases the score
    private void checkFullLines() {
        int linesCleared = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean fullLine = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[j][i] == Tetromino.Empty) {
                    fullLine = false;
                    break;
                }
            }

            if (fullLine) {
                clearFullLines(i);

                linesCleared++;
                i++;
            }
        }

        if (linesCleared > 0) {
            score += linesCleared;
            EventLog.getInstance().logEvent(new Event(linesCleared + " line(s) cleared."));
        }

        setChanged();
        notifyObservers(EVENT_LINE_CLEARED);
    }

    private void clearFullLines(int i) {
        for (int k = i; k > 0; k--) {
            for (int l = 0; l < BOARD_WIDTH; l++) {
                board[l][k] = board[l][k - 1];
            }
        }

        for (int k = 0; k < BOARD_WIDTH; k++) {
            board[k][0] = Tetromino.Empty;
        }
    }

    // MODIFIES: this
    // EFFECT: if the new block is spawned on an already occupied tile, sets ended to true
    private void checkGameOver() {
        for (Position pos : curBlock.getAllPosOnBoard()) {
            if (board[pos.getX()][pos.getY()] != Tetromino.Empty) {
                ended = true;
                EventLog.getInstance().logEvent(new Event("Game ended with " + score + " line(s) cleared."));
                break;
            }
        }
    }

    // MODIFIES: this
    // EFFECT: sets every tile on the board to empty
    private void clearBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = Tetromino.Empty;
            }
        }
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(Boolean isEnded) {
        ended = isEnded;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int s) {
        score = s;
    }

    public Block getCurBlock() {
        return curBlock;
    }

    public void setCurBlock(Block block) {
        curBlock = block;
    }

    public Tetromino[][] getBoard() {
        return board;
    }

    public void setBoard(Tetromino[][] b) {
        board = b;
    }

    public int getTicksUntilFall() {
        return ticksUntilFall;
    }

    public void setTicksUntilFall(int ticks) {
        ticksUntilFall = ticks;
    }

    // MODIFIES: this
    // EFFECT: resets ticksUntilFall
    private void resetFall() {
        ticksUntilFall = TICKS_PER_FALL;
    }

    //EFFECTS: returns all the fields in game as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("ended", ended);
        json.put("score", score);
        json.put("curBlock", curBlock.toJson());
        json.put("board", boardToJson());
        json.put("ticksUntilFall", ticksUntilFall);
        return json;
    }

    //EFFECTS: returns board as a JSONArray
    private JSONArray boardToJson() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < BOARD_WIDTH; i++) {
            JSONArray columnJsonArray = new JSONArray();

            for (Tetromino t : board[i]) {
                columnJsonArray.put(t.toString());
            }

            jsonArray.put(columnJsonArray);
        }

        return jsonArray;
    }
}
