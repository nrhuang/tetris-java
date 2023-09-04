package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A testing class for Block
 */
class TetrominoTest {
    Block block;

    @BeforeEach
    void runBefore() {
        block = new Block();
    }

    @Test
    public void testGetLabelLineShape() {
        block.setShape(Tetromino.valueOf("LineShape"));
        assertEquals("LineShape", block.getShape().getLabel());
    }

    @Test
    public void testGetLabelZShape() {
        block.setShape(Tetromino.valueOf("ZShape"));
        assertEquals("ZShape", block.getShape().getLabel());
    }
}
