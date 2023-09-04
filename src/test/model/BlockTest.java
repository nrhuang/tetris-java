package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A testing class for Block
 */
class BlockTest {
    private Block block;

    @BeforeEach
    void runBefore() {
        block = new Block();
    }

    @Test
    public void testMoveRight() {
        int initCentreX = block.getCentrePosOnBoard().getX();
        int initCentreY = block.getCentrePosOnBoard().getY();
        block.setNextMove(Moves.Right);
        block.move();

        assertEquals(initCentreX + 1, block.getCentrePosOnBoard().getX());
        assertEquals(initCentreY, block.getCentrePosOnBoard().getY());
        assertEquals(Moves.None, block.getNextMove());
    }

    @Test
    public void testMoveLeft() {
        int initCentreX = block.getCentrePosOnBoard().getX();
        int initCentreY = block.getCentrePosOnBoard().getY();
        block.setNextMove(Moves.Left);
        block.move();

        assertEquals(initCentreX - 1, block.getCentrePosOnBoard().getX());
        assertEquals(initCentreY, block.getCentrePosOnBoard().getY());
        assertEquals(Moves.None, block.getNextMove());
    }

    @Test
    public void testMoveDown() {
        int initCentreX = block.getCentrePosOnBoard().getX();
        int initCentreY = block.getCentrePosOnBoard().getY();
        block.setNextMove(Moves.Down);
        block.move();

        assertEquals(initCentreX, block.getCentrePosOnBoard().getX());
        assertEquals(initCentreY + 1, block.getCentrePosOnBoard().getY());
        assertEquals(Moves.None, block.getNextMove());
    }

    @Test
    public void testMoveCW() {
        while (block.getShape() == Tetromino.SquareShape) {
            block = new Block();
        }
        int[][] initRelPos = new int[4][2];
        for (int i = 0; i < 4; i++) {
            initRelPos[i][0] = block.getRelativePos()[i].getX();
            initRelPos[i][1] = block.getRelativePos()[i].getY();
        }
        block.setNextMove(Moves.CW);
        block.move();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][1] * -1, block.getRelativePos()[i].getX());
            assertEquals(initRelPos[i][0], block.getRelativePos()[i].getY());
        }

        assertEquals(Moves.None, block.getNextMove());
    }

    @Test
    public void testMoveCCW() {
        while (block.getShape() == Tetromino.SquareShape) {
            block = new Block();
        }
        int[][] initRelPos = new int[4][2];
        for (int i = 0; i < 4; i++) {
            initRelPos[i][0] = block.getRelativePos()[i].getX();
            initRelPos[i][1] = block.getRelativePos()[i].getY();
        }
        block.setNextMove(Moves.CCW);
        block.move();

        for (int i = 0; i < 4; i++) {
            assertEquals(initRelPos[i][1], block.getRelativePos()[i].getX());
            assertEquals(initRelPos[i][0] * -1, block.getRelativePos()[i].getY());
        }

        assertEquals(Moves.None, block.getNextMove());
    }

    @Test
    public void testFall() {
        int initCentreX = block.getCentrePosOnBoard().getX();
        int initCentreY = block.getCentrePosOnBoard().getY();
        block.fall();

        assertEquals(initCentreX, block.getCentrePosOnBoard().getX());
        assertEquals(initCentreY + 1, block.getCentrePosOnBoard().getY());
        assertEquals(Moves.None, block.getNextMove());
    }

    @Test
    public void testDraw() {

    }
}
