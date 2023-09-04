package persistence;

import model.Game;
import model.Moves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for JsonReader
 * Designed based on JsonWriterTest from JsonSerializationDemo
 */
class JsonReaderTest extends JsonTest {
    private Game game;

    @BeforeEach
    void runBefore() {
        game = new Game();
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            game = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFile() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFile.json");
        try {
            assertNull(reader.read());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    void testReaderNewGame() throws FileNotFoundException {
        JsonWriter writer = new JsonWriter("./data/testReaderNewGame.json");
        writer.open();
        writer.write(game);
        writer.close();
        JsonReader reader = new JsonReader("./data/testReaderNewGame.json");
        try {
            Game newGame = reader.read();
            checkGame(game, newGame);
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    void testReaderGeneralGame() throws FileNotFoundException {
        game.setScore(3);
        game.getCurBlock().setNextMove(Moves.Drop);
        game.tick();
        game.getCurBlock().setNextMove(Moves.CW);
        JsonWriter writer = new JsonWriter("./data/testReaderGeneralGame.json");
        writer.open();
        writer.write(game);
        writer.close();
        JsonReader reader = new JsonReader("./data/testReaderGeneralGame.json");
        try {
            Game newGame = reader.read();
            checkGame(game, newGame);
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
}