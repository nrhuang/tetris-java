package ui;

import model.Event;
import model.EventLog;
import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Represents the whole Tetris game with visual ui
 */
public class Tetris extends JFrame {
    private static final String JSON_FILE = "./data/game.json";
    private static final int INTERVAL = 20;
    private static Game game;
    private GamePanel gp;
    private ScorePanel sp;
    private MenuPanel mp;
    private JDialog menu;
    private ActionListener actionListener;
    private Timer timer;

    // EFFECTS: initializes game, panels, key listener, timer, and observers
    public Tetris() {
        super("Tetris");
        setUndecorated(true);
        game = new Game();
        gp = new GamePanel(game);
        sp = new ScorePanel(0);
        mp = new MenuPanel();
        add(gp);
        add(sp, BorderLayout.NORTH);
        addKeyListener(new KeyHandler());
        pack();
        centreOnScreen();
        setVisible(true);
        addTimer();
        timer.start();
        game.addObserver(sp);
    }

    // EFFECTS: initializes a timer that updates game each INTERVAL milliseconds
    private void addTimer() {
        timer = new Timer(INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                game.tick();
                gp.repaint();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    /**
     * A key handler to respond to key events
     */
    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                timer.stop();
                openMenu();
            } else {
                game.keyPressed(e.getKeyCode());
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: opens pause menu dialog with buttons
    private void openMenu() {
        menu = new JDialog(this);
        menu.getContentPane().add(mp);
        menu.setUndecorated(true);
        menu.pack();
        menu.setLocationRelativeTo(this);
        menu.setVisible(true);
        addButtons(menu);
        add(mp);
    }

    // MODIFIES: this
    // EFFECTS: adds buttons and listeners to menu
    private void addButtons(JDialog menu) {
        JButton resume = new JButton("Resume");
        resume.setBounds(5, 5, 80, 30);
        menu.add(resume);
        JButton save = new JButton("Save");
        save.setBounds(5, 35, 80, 30);
        menu.add(save);
        JButton load = new JButton("Load");
        load.setBounds(5, 65, 80, 30);
        menu.add(load);
        JButton exit = new JButton("Exit");
        exit.setBounds(5, 95, 80, 30);
        menu.add(exit);
        JButton empty = new JButton("");
        empty.setBounds(0, 0, 0, 0);
        menu.add(empty);

        createActionListener();

        resume.addActionListener(actionListener);
        save.addActionListener(actionListener);
        load.addActionListener(actionListener);
        exit.addActionListener(actionListener);
    }

    // EFFECTS: creates action listener for buttons
    private void createActionListener() {
        actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    handleAction(ae);
                } catch (IOException e) {
                    System.out.println("IOException caught");
                }
            }
        };
    }

    // MODIFIES: this
    // EFFECTS: handles button presses for resume, save, load, and exit
    private void handleAction(ActionEvent ae) throws IOException {
        switch (ae.getActionCommand()) {
            case "Resume":
                timer.start();
                menu.setVisible(false);
                break;
            case "Save":
                saveGame();
                break;
            case "Load":
                loadGame();
                break;
            case "Exit":
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event.toString() + "\n");
                }
                System.exit(0);
        }
    }

    // EFFECTS: starts the game
    public static void main(String[] args) {
        new Tetris();
    }

    // EFFECTS: saves the game to file
    private void saveGame() throws IOException {
        JsonWriter jsonWriter = new JsonWriter(JSON_FILE);
        jsonWriter.open();
        jsonWriter.write(game);
        jsonWriter.close();
    }

    // MODIFIES: this
    // EFFECTS: loads the game from file and updates the game and score panels
    private void loadGame() throws IOException {
        JsonReader jsonReader = new JsonReader(JSON_FILE);
        if (jsonReader.read() != null) {
            game = jsonReader.read();
            gp.setGame(game);
            sp = new ScorePanel(game.getScore());
            add(sp, BorderLayout.NORTH);
            game.addObserver(sp);
            gp.repaint();
        }
    }
}
