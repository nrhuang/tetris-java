package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Methods for comparing games, blocks, or positions
 */
public class JsonTest {
    public void checkGame(Game game, Game newGame) {
        assertEquals(game.isEnded(), newGame.isEnded());
        assertEquals(game.getScore(), newGame.getScore());
        checkBlock(game.getCurBlock(), newGame.getCurBlock());
        checkBoard(game.getBoard(), newGame.getBoard());
        assertEquals(game.getTicksUntilFall(), newGame.getTicksUntilFall());
    }

    protected void checkBlock(Block block, Block newBlock) {
        assertEquals(block.getShape(), newBlock.getShape());
        checkPosition(block.getCentrePosOnBoard(), newBlock.getCentrePosOnBoard());
        for (int i = 0; i < 4; i++) {
            checkPosition(block.getRelativePos()[i], newBlock.getRelativePos()[i]);
        }
        assertEquals(block.getNextMove(), newBlock.getNextMove());
    }

    protected void checkPosition(Position pos, Position newPos) {
        assertEquals(pos.getX(), newPos.getX());
        assertEquals(pos.getY(), newPos.getY());
    }

    protected void checkBoard(Tetromino[][] board, Tetromino[][] newBoard) {
        for (int i = 0; i < Game.BOARD_WIDTH; i++) {
            for (int j = 0; j < Game.BOARD_HEIGHT; j++) {
                assertEquals(board[i][j], newBoard[i][j]);
            }
        }
    }
}
