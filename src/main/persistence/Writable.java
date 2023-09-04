package persistence;

import org.json.JSONObject;

/**
 * An interface for writable objects
 * Designed based on Writable from JsonSerializationDemo
 */
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
