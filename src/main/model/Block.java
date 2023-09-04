package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Represents the current falling block that the user controls
 */
public class Block implements Writable {
    private Tetromino shape;
    private Position centrePosOnBoard;
    private Position[] relativePos = new Position[4];
    private Moves nextMove;

    // MODIFIES: this
    // EFFECT: creates a new block with a random shape, sets the centre tile to (4,1), sets nextMove to None
    public Block() {
        setShape(randomTetromino());
        centrePosOnBoard = new Position(4,1);
        nextMove = Moves.None;
    }

    // MODIFIES: this
    // EFFECTS: moves the block according to the move in nextMove, then sets nextMove to None
    public void move() {
        switch (nextMove) {
            case Right:
                this.moveRight();
                break;
            case Left:
                this.moveLeft();
                break;
            case Down:
                this.moveDown();
                break;
            case CW:
                this.rotateCW();
                break;
            case CCW:
                this.rotateCCW();
                break;
        }

        this.setNextMove(Moves.None);
    }

    // EFFECT: public method that calls moveDown()
    public void fall() {
        this.moveDown();
    }

    // MODIFIES: this
    // EFFECTS: sets the shape and the relative positions of each tile with respect to the centre tile
    public void setShape(Tetromino t) {
        this.shape = t;

        Position[][] relPosTable = new Position[][]{
            {new Position(0, 0), new Position(0, 0), new Position(0, 0), new Position(0, 0)},
            {new Position(-1, 0), new Position(0, 0), new Position(1, 0), new Position(2, 0)},
            {new Position(0, 0), new Position(1, 0), new Position(0, -1), new Position(1, -1)},
            {new Position(-1, 0), new Position(0, 0), new Position(1, 0), new Position(0, -1)},
            {new Position(-1, 0), new Position(0, 0), new Position(1, 0), new Position(1, -1)},
            {new Position(-1, -1), new Position(-1, 0), new Position(0, 0), new Position(1, 0)},
            {new Position(-1, 0), new Position(0, 0), new Position(0, -1), new Position(1, -1)},
            {new Position(-1, -1), new Position(0, -1), new Position(0, 0), new Position(1, 0)}
        };

        for (int i = 0; i < 4; i++) {
            System.arraycopy(relPosTable[shape.ordinal()], 0, relativePos, 0, 4);
        }
    }

    // EFFECTS: adds the coordinates of the centre tile to the coordinates of each
    //          relative tile to get the actual coordinates of each tile on the board
    public Position[] getAllPosOnBoard() {
        Position[] allPosOnBoard = new Position[4];
        for (int i = 0; i < 4; i++) {
            int newX = centrePosOnBoard.getX() + relativePos[i].getX();
            int newY = centrePosOnBoard.getY() + relativePos[i].getY();
            Position posOnBoard = new Position(newX, newY);
            allPosOnBoard[i] = posOnBoard;
        }

        return allPosOnBoard;
    }

    public Tetromino getShape() {
        return shape;
    }

    public Position getCentrePosOnBoard() {
        return centrePosOnBoard;
    }

    public void setCentrePosOnBoard(Position pos) {
        centrePosOnBoard = pos;
    }

    public Position[] getRelativePos() {
        return relativePos;
    }

    public void setRelativePos(Position[] relPos) {
        relativePos = relPos;
    }

    public Moves getNextMove() {
        return nextMove;
    }

    public void setNextMove(Moves move) {
        this.nextMove = move;
    }

    // EFFECTS: chooses a random tetromino excluding the Empty and Background
    private Tetromino randomTetromino() {
        List<Tetromino> tetrominoList = Arrays.asList(Tetromino.values());
        int size = tetrominoList.size();
        Random random = new Random();

        return tetrominoList.get(random.nextInt(size - 2) + 1);
    }

    // MODIFIES: this
    // EFFECTS: move the centre tile of the block right by 1
    private void moveRight() {
        centrePosOnBoard.setX(centrePosOnBoard.getX() + 1);
    }

    // MODIFIES: this
    // EFFECTS: move the centre tile of the block left by 1
    private void moveLeft() {
        centrePosOnBoard.setX(centrePosOnBoard.getX() - 1);
    }

    // MODIFIES: this
    // EFFECTS: move the centre tile of the block down by 1
    private void moveDown() {
        centrePosOnBoard.setY(centrePosOnBoard.getY() + 1);
    }

    // MODIFIES: this
    // EFFECTS: rotate the block clockwise about the centre tile
    private void rotateCW() {
        if (this.shape != Tetromino.SquareShape) {
            for (Position pos : relativePos) {
                int x = pos.getX();
                int y = pos.getY();
                pos.setX(y * -1);
                pos.setY(x);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: rotate the block counter-clockwise about the centre tile
    private void rotateCCW() {
        if (this.shape != Tetromino.SquareShape) {
            for (Position pos : relativePos) {
                int x = pos.getX();
                int y = pos.getY();
                pos.setX(y);
                pos.setY(x * -1);
            }
        }
    }

    //EFFECTS: returns all the fields of this as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("shape", shape.toString());
        json.put("centrePosOnBoard", centrePosOnBoard.toJson());
        json.put("relativePos", posArrayToJson());
        json.put("nextMove", nextMove.toString());
        return json;
    }

    //EFFECTS: returns relativePos as a JSONArray
    private JSONArray posArrayToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Position p : relativePos) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }
}
