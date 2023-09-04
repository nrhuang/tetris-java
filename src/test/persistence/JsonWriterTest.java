package persistence;

import model.Game;
import model.Moves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A test class for JsonWriter
 * Designed based on JsonWriterTest from JsonSerializationDemo
 */
class JsonWriterTest extends JsonTest {
    private Game game;

    @BeforeEach
    void runBefore() {
        game = new Game();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterNewGame() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterNewGame.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNewGame.json");
            Game newGame = reader.read();
            checkGame(game, newGame);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGame() {
        try {
            game.setScore(2);
            game.getCurBlock().setNextMove(Moves.Drop);
            game.tick();
            game.getCurBlock().setNextMove(Moves.Right);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralGame.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralGame.json");
            Game newGame = reader.read();
            checkGame(game, newGame);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}