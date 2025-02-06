package core.parser;

import org.json.JSONObject;

/**
 * The JsonDroneParser interface defines basic functions for parsing and validating JSON objects into drone objects.
 * Implementations of this interface are responsible for converting API responses into specific drone types.
 * @param <T> The type of drone object that this parser uses.
 */
public interface JsonDroneParser<T> {
    T parse(JSONObject obj);
    boolean isValid(JSONObject obj);
    String getEndpoint();
}