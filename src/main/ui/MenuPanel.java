package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the panel in which the pause menu is displayed
 */
public class MenuPanel extends JPanel {

    // EFFECTS: sets size, background color, and grid layout of panel
    public MenuPanel() {
        setPreferredSize(new Dimension(90, 130));
        setBackground(Color.LIGHT_GRAY);
        setLayout(new GridLayout(1,4));
    }
}
