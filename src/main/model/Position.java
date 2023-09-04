package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * Represents a position with coordinates x,y
 */
public class Position implements Writable {
    private int posX;
    private int posY;

    public Position(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public int getX() {
        return posX;
    }

    public void setX(int x) {
        this.posX = x;
    }

    public int getY() {
        return posY;
    }

    public void setY(int posY) {
        this.posY = posY;
    }

    //EFFECTS: returns all the fields of this as a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("x", posX);
        json.put("y", posY);
        return json;
    }
}
