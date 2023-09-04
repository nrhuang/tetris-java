package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonTest;

import java.awt.event.KeyEvent;

import static model.Game.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A testing class for Game
 */
class GameTest {
    private Game game;

    @BeforeEach
    void runBefore() {
        game = new Game();
    }

    @Test
    public void testTickGameOver() {
        game.setEnded(true);
        game.tick();
        assertEquals(TICKS_PER_FALL, game.getTicksUntilFall());
    }

    @Test
    public void testTickDrop() {
        game.getCurBlock().setNextMove(Moves.Drop);
        Tetromino shape = game.getCurBlock().getShape();
        Position[] relPos = game.getCurBlock().getRelativePos();
        game.tick();

        for (Position pos : relPos) {
            int expectedX = pos.getX() + 4;
            int expectedY = pos.getY() + BOARD_HEIGHT - 1;
            assertEquals(shape, game.getBoard()[expectedX][expectedY]);
        }

        assertEquals(4, game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(1, game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    // MODIFIES: this
    // EFFECTS: returns the initial coordinates of the centre tile and
    //          ticks the game with the given move
    private int[] setupTestMoveSuccess(Moves move) {
        int[] initCentre = new int[2];
        initCentre[0] = game.getCurBlock().getCentrePosOnBoard().getX();
        initCentre[1] = game.getCurBlock().getCentrePosOnBoard().getY();
        game.getCurBlock().setNextMove(move);
        game.tick();

        return initCentre;
    }

    // MODIFIES: this
    // EFFECTS: returns the initial coordinates of the centre tile,
    //          moves the block to the boundary, then ticks the game with the given move
    private int[] setupTestMoveFail(Moves move, int distToBoundary) {
        for (int i = 0; i < distToBoundary; i++) {
            game.getCurBlock().setNextMove(move);
            game.tick();
        }

        int[] initCentre = new int[2];
        initCentre[0] = game.getCurBlock().getCentrePosOnBoard().getX();
        initCentre[1] = game.getCurBlock().getCentrePosOnBoard().getY();
        game.getCurBlock().setNextMove(move);
        game.tick();

        return initCentre;
    }

    @Test
    public void testTickMoveRightSuccess() {
        int[] initCentre = setupTestMoveSuccess(Moves.Right);

        assertEquals(initCentre[0] + 1, game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1], game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickMoveRightFail() {
        int distToRightWall = 4;

        if (game.getCurBlock().getShape() == Tetromino.LineShape) {
            distToRightWall = 3;
        }

        int[] initCentre = setupTestMoveFail(Moves.Right, distToRightWall);

        assertEquals(initCentre[0], game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1], game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - distToRightWall - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickMoveLeftSuccess() {
        int[] initCentre = setupTestMoveSuccess(Moves.Left);

        assertEquals(initCentre[0] - 1, game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1], game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickMoveLeftFail() {
        int distToLeftWall = 3;

        if (game.getCurBlock().getShape() == Tetromino.SquareShape) {
            distToLeftWall = 4;
        }

        int[] initCentre = setupTestMoveFail(Moves.Left, distToLeftWall);

        assertEquals(initCentre[0], game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1], game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - distToLeftWall - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickMoveDownSuccess() {
        int[] initCentre = setupTestMoveSuccess(Moves.Down);

        assertEquals(initCentre[0], game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1] + 1, game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickMoveDownFail() {
        int distToBottom = 18;

        int[] initCentre = setupTestMoveFail(Moves.Down, distToBottom);

        assertEquals(initCentre[0], game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1], game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 2, game.getTicksUntilFall());
    }

    @Test
    public void testTickMoveNone() {
        int[] initCentre = setupTestMoveSuccess(Moves.None);

        assertEquals(initCentre[0], game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1], game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    // MODIFIES: this
    // EFFECTS: returns the initial relative positions of each tile with respect to the centre tile,
    //          then sets the next move as the given move
    private int[][] setupTestTickRotate(Moves move, boolean isSquare) {
        if (!isSquare) {
            while (game.getCurBlock().getShape() == Tetromino.SquareShape) {
                game = new Game();
            }
        }

        int[][] initRelPos = new int[4][2];

        for (int i = 0; i < 4; i++) {
            initRelPos[i][0] = game.getCurBlock().getRelativePos()[i].getX();
            initRelPos[i][1] = game.getCurBlock().getRelativePos()[i].getY();
        }

        game.getCurBlock().setNextMove(move);
        return initRelPos;
    }

    @Test
    public void testTickMoveCWSuccess() {
        int[][] initRelPos = setupTestTickRotate(Moves.CW, false);

        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][1] * -1, game.getCurBlock().getRelativePos()[i].getX());
            assertEquals(initRelPos[i][0], game.getCurBlock().getRelativePos()[i].getY());
            assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
        }
    }

    @Test
    public void testTickMoveCWFail() {
        int[][] initRelPos = setupTestTickRotate(Moves.CW, false);

        if (game.getCurBlock().getShape() == Tetromino.ZShape) {
            game.getBoard()[5][2] = Tetromino.LineShape;
        } else {
            game.getBoard()[4][2] = Tetromino.LineShape;
        }

        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][0], game.getCurBlock().getRelativePos()[i].getX());
            assertEquals(initRelPos[i][1], game.getCurBlock().getRelativePos()[i].getY());
            assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
        }
    }

    @Test
    public void testTickMoveCWSquare() {
        game.getCurBlock().setShape(Tetromino.SquareShape);
        int[][] initRelPos = setupTestTickRotate(Moves.CW, true);
        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][0], game.getCurBlock().getRelativePos()[i].getX());
            assertEquals(initRelPos[i][1], game.getCurBlock().getRelativePos()[i].getY());
            assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
        }
    }

    @Test
    public void testTickMoveCCWSuccess() {
        int[][] initRelPos = setupTestTickRotate(Moves.CCW, false);

        if (game.getCurBlock().getShape() == Tetromino.LineShape) {
            game.getCurBlock().fall();
            game.getCurBlock().setNextMove(Moves.CCW);
        }

        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][1], game.getCurBlock().getRelativePos()[i].getX());
            assertEquals(initRelPos[i][0] * -1, game.getCurBlock().getRelativePos()[i].getY());
            assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
        }
    }

    @Test
    public void testTickMoveCCWFail() {
        int[][] initRelPos = setupTestTickRotate(Moves.CCW, false);

        if (game.getCurBlock().getShape() == Tetromino.SShape) {
            game.getBoard()[3][2] = Tetromino.LineShape;
        } else {
            game.getBoard()[4][2] = Tetromino.LineShape;
        }

        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][0], game.getCurBlock().getRelativePos()[i].getX());
            assertEquals(initRelPos[i][1], game.getCurBlock().getRelativePos()[i].getY());
            assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
        }
    }

    @Test
    public void testTickMoveCCWSquare() {
        game.getCurBlock().setShape(Tetromino.SquareShape);
        int[][] initRelPos = setupTestTickRotate(Moves.CCW, true);
        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][0], game.getCurBlock().getRelativePos()[i].getX());
            assertEquals(initRelPos[i][1], game.getCurBlock().getRelativePos()[i].getY());
            assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
        }
    }

    @Test
    public void testTickFallSuccess() {
        for (int i = 0; i < TICKS_PER_FALL; i++) {
            game.tick();
        }

        int[] initCentre = setupTestMoveSuccess(Moves.None);

        assertEquals(initCentre[0], game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(initCentre[1] + 1, game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickFallFail() {
        int distToBottom = 18;

        for (int i = 0; i < distToBottom; i++) {
            game.getCurBlock().setNextMove(Moves.Down);
            game.tick();
        }

        Tetromino shape = game.getCurBlock().getShape();
        Position[] positions = game.getCurBlock().getAllPosOnBoard();

        for (int i = 0; i < TICKS_PER_FALL; i++) {
            game.tick();
        }

        for (int i = 0; i < 4; i++) {
            assertEquals(shape, game.getBoard()[positions[i].getX()][positions[i].getY()]);
        }

        assertEquals(4, game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(1, game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickFallFailClearOneLine() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            game.getBoard()[i][3] = Tetromino.LineShape;
        }

        game.getBoard()[0][0] = Tetromino.LineShape;
        game.getBoard()[0][1] = Tetromino.LineShape;

        for (int i = 0; i < TICKS_PER_FALL * 2; i++) {
            game.tick();
        }

        Tetromino shape = game.getCurBlock().getShape();
        Position[] positions = game.getCurBlock().getAllPosOnBoard();
        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(shape, game.getBoard()[positions[i].getX()][positions[i].getY() + 1]);
        }

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                boolean expectedEmpty = true;

                for (int k = 0; k < 4; k++) {
                    if (i == positions[k].getX() && j == positions[k].getY() + 1) {
                        expectedEmpty = false;
                        break;
                    }
                }

                if (i == 0 && (j == 1 || j == 2)) {
                    expectedEmpty = false;
                }

                if (expectedEmpty) {
                    assertEquals(Tetromino.Empty, game.getBoard()[i][j]);
                }
            }
        }

        assertEquals(Tetromino.LineShape, game.getBoard()[0][1]);
        assertEquals(Tetromino.LineShape, game.getBoard()[0][2]);
        assertEquals(1, game.getScore());
        assertEquals(4, game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(1, game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickFallFailClearThreeLines() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            game.getBoard()[i][3] = Tetromino.LineShape;
            game.getBoard()[i][4] = Tetromino.LineShape;
            game.getBoard()[i][10] = Tetromino.LineShape;
        }

        game.getBoard()[0][0] = Tetromino.LineShape;
        game.getBoard()[0][1] = Tetromino.LineShape;

        for (int i = 0; i < TICKS_PER_FALL * 2; i++) {
            game.tick();
        }

        Tetromino shape = game.getCurBlock().getShape();
        Position[] positions = game.getCurBlock().getAllPosOnBoard();
        game.tick();

        for (int i = 0; i < 4; i++) {
            assertEquals(shape, game.getBoard()[positions[i].getX()][positions[i].getY() + 3]);
        }

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                boolean expectedEmpty = true;

                for (int k = 0; k < 4; k++) {
                    if (i == positions[k].getX() && j == positions[k].getY() + 3) {
                        expectedEmpty = false;
                        break;
                    }
                }

                if (i == 0 && (j == 3 || j == 4)) {
                    expectedEmpty = false;
                }

                if (expectedEmpty) {
                    assertEquals(Tetromino.Empty, game.getBoard()[i][j]);
                }
            }
        }

        assertEquals(Tetromino.LineShape, game.getBoard()[0][3]);
        assertEquals(Tetromino.LineShape, game.getBoard()[0][4]);
        assertEquals(3, game.getScore());
        assertEquals(4, game.getCurBlock().getCentrePosOnBoard().getX());
        assertEquals(1, game.getCurBlock().getCentrePosOnBoard().getY());
        assertEquals(TICKS_PER_FALL - 1, game.getTicksUntilFall());
    }

    @Test
    public void testTickFallFailGameOver() {
        Tetromino shape = game.getCurBlock().getShape();
        Position[] positions = game.getCurBlock().getAllPosOnBoard();
        game.getBoard()[4][2] = Tetromino.LineShape;

        for (int i = 0; i < TICKS_PER_FALL + 1; i++) {
            game.tick();
        }

        for (int i = 0; i < 4; i++) {
            assertEquals(shape, game.getBoard()[positions[i].getX()][positions[i].getY()]);
        }

        assertTrue(game.isEnded());
    }

    @Test
    public void testKeyPressed() {
        game.keyPressed(KeyEvent.VK_RIGHT);
        assertEquals(Moves.Right, game.getCurBlock().getNextMove());
        game.keyPressed(KeyEvent.VK_LEFT);
        assertEquals(Moves.Left, game.getCurBlock().getNextMove());
        game.keyPressed(KeyEvent.VK_DOWN);
        assertEquals(Moves.Down, game.getCurBlock().getNextMove());
        game.keyPressed(KeyEvent.VK_UP);
        assertEquals(Moves.CW, game.getCurBlock().getNextMove());
        game.keyPressed(KeyEvent.VK_Z);
        assertEquals(Moves.CCW, game.getCurBlock().getNextMove());
        game.keyPressed(KeyEvent.VK_SPACE);
        assertEquals(Moves.Drop, game.getCurBlock().getNextMove());
        game.keyPressed(KeyEvent.VK_C);
        assertEquals(Moves.None, game.getCurBlock().getNextMove());
    }

    @Test
    public void testToJson() {
        JSONObject json = game.toJson();
        JsonReader reader = new JsonReader(null);
        Game newGame = reader.parseGame(json);
        JsonTest jsonTest = new JsonTest();
        jsonTest.checkGame(game, newGame);
    }
}