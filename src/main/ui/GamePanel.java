package ui;

import model.Game;
import model.Position;
import model.Tetromino;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the panel in which the game is rendered.
 * Designed based on GamePanel from SpaceInvadersRefactored-ObserverLessCoupling
 */
public class GamePanel extends JPanel {

    public static final int TETROMINO_SIZE = 20;
    private static final String OVER = "Game Over!";
    private static final String REPLAY = "R to replay";
    private Game game;

    // EFFECTS: initializes game panel with size, background colour, and game to be displayed
    public GamePanel(Game game) {
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        setBackground(Color.BLACK);
        this.game = game;
    }

    // MODIFIES: g
    // EFFECTS: draws the game on the panel, if game is over, draws the end screen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGame(g);

        if (game.isEnded()) {
            gameOver(g);
        }
    }

    // MODIFIES: g
    // EFFECTS: draws each Tetromino in the board on the screen
    private void drawGame(Graphics g) {
        for (int i = 0; i < Game.BOARD_WIDTH; i++) {
            for (int j = 0; j < Game.BOARD_HEIGHT; j++) {
                drawTetromino(g, game.getBoard()[i][j], i * Tetromino.SIZE, j * Tetromino.SIZE);
            }
        }

        drawBlock(g);
    }

    // MODIFIES: g
    // EFFECTS: draws a Tetromino for each tile in the block onto the screen
    private void drawBlock(Graphics g) {
        for (Position p : game.getCurBlock().getAllPosOnBoard()) {
            drawTetromino(g, game.getCurBlock().getShape(), p.getX() * Tetromino.SIZE, p.getY() * Tetromino.SIZE);
        }
    }

    // MODIFIES: g
    // EFFECTS: draws a square at the given coordinates with color this.color
    public void drawTetromino(Graphics g, Tetromino t, int x, int y) {
        String sep = System.getProperty("file.separator");
        String sys = System.getProperty("user.dir");
        ImageIcon img = new ImageIcon(sys + sep + "data" + sep + "images" + sep + t.getLabel() + ".png");
        img.paintIcon(this, g, x, y);
    }

    // MODIFIES: g
    // EFFECTS: draws game over message onto g
    private void gameOver(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString(OVER, g, fm, Game.HEIGHT / 2);
    }

    // MODIFIES: g
    // EFFECTS: centres the string str horizontally onto g at vertical position y
    private void centreString(String str, Graphics g, FontMetrics fm, int y) {
        int width = fm.stringWidth(str);
        g.drawString(str, (Game.WIDTH - width) / 2, y);
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
