package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Represents a reader that reads game from JSON data stored in file
 * Designed based on JsonReader from JsonSerializationDemo
 */
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads game from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public Game read() throws IOException {
        String jsonData = readFile(source);
        if (jsonData.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses game from JSON object and returns it
    public Game parseGame(JSONObject jsonObject) {
        Boolean ended = jsonObject.getBoolean("ended");
        int score = jsonObject.getInt("score");
        Block curBlock = parseBlock(jsonObject.getJSONObject("curBlock"));
        Tetromino[][] board = parseBoard(jsonObject.getJSONArray("board"));
        int ticksUntilFall = jsonObject.getInt("ticksUntilFall");

        Game game = new Game();
        game.setEnded(ended);
        game.setScore(score);
        game.setCurBlock(curBlock);
        game.setBoard(board);
        game.setTicksUntilFall(ticksUntilFall);
        return game;
    }

    // EFFECTS: parses block from JSON object and returns it
    private Block parseBlock(JSONObject jsonObject) {
        Tetromino shape = Tetromino.valueOf(jsonObject.getString("shape"));
        Position centrePosOnBoard = parsePosition(jsonObject.getJSONObject("centrePosOnBoard"));
        Position[] relativePos = parsePositionArray(jsonObject.getJSONArray("relativePos"));
        Moves nextMove = Moves.valueOf(jsonObject.getString("nextMove"));

        Block block = new Block();
        block.setShape(shape);
        block.setCentrePosOnBoard(centrePosOnBoard);
        block.setRelativePos(relativePos);
        block.setNextMove(nextMove);
        return block;
    }

    // EFFECTS: parses position from JSON object and returns it
    private Position parsePosition(JSONObject jsonObject) {
        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");

        return new Position(x, y);
    }

    // EFFECTS: parses position array from JSON array and returns it
    private Position[] parsePositionArray(JSONArray jsonArray) {
        Position[] posArr = new Position[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            posArr[i] = parsePosition(jsonArray.getJSONObject(i));
        }

        return posArr;
    }

    // EFFECTS: parses board from JSON array and returns it
    private Tetromino[][] parseBoard(JSONArray jsonArray) {
        Tetromino[][] board = new Tetromino[Game.BOARD_WIDTH][Game.BOARD_HEIGHT];

        for (int i = 0; i < Game.BOARD_WIDTH; i++) {
            for (int j = 0; j < Game.BOARD_HEIGHT; j++) {
                board[i][j] = Tetromino.valueOf(jsonArray.getJSONArray(i).getString(j));
            }
        }

        return board;
    }
}
