package model;

/**
 * Enumeration of possible shapes that each tile can have
 */
public enum Tetromino {
    Empty("Empty"),
    LineShape("LineShape"),
    SquareShape("SquareShape"),
    TShape("TShape"),
    LShape("LShape"),
    MirroredLShape("MirroredLShape"),
    ZShape("ZShape"),
    SShape("SShape");

    public static final int SIZE = 20;
    private final String label;

    // EFFECTS: initializes a tetromino with a label and a corresponding image icon
    Tetromino(final String label) {
        this.label = label;

    }

    public String getLabel() {
        return label;
    }
}
