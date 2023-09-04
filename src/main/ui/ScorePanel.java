package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents the panel in which the scoreboard is displayed.
 * Designed based on ScorePanel from SpaceInvadersRefactored-ObserverLessCoupling
 */
public class ScorePanel extends JPanel implements Observer {
    private static final String SCORE_TXT = "Lines Cleared: ";
    private static final int LBL_HEIGHT = 30;
    private JLabel scoreLabel;

    // EFFECTS: sets the background colour and draws the initial label;
    //          updates this with the game whose score is to be displayed
    public ScorePanel(int score) {
        setBackground(new Color(180, 180, 180));
        scoreLabel = new JLabel(SCORE_TXT + score);
        scoreLabel.setPreferredSize(new Dimension(Game.WIDTH, LBL_HEIGHT));
        add(scoreLabel);
        add(Box.createHorizontalStrut(10));
    }

    // MODIFIES: this
    // EFFECTS: updates the score to reflect the current state of the game
    @Override
    public void update(Observable subject, Object event) {
        // NOTE: the following check is not strictly needed, given that we have only two types
        // of events; we include it here in case we add another kind of event in future to
        // which this observer need not respond.
        if (Game.EVENT_LINE_CLEARED.equals(event)) {
            Game game = (Game) subject;
            scoreLabel.setText(SCORE_TXT + game.getScore());
            repaint();
        }
    }
}
