package core.parser;

import org.json.JSONObject;

/**
 * The {@code JsonDroneParser} interface defines methods for parsing and validating JSON objects into drone objects.
 * Implementations of this interface are responsible for converting API responses into specific drone types.
 *
 * @param <T> The type of drone object that this parser handles.
 */
public interface JsonDroneParser<T> {

    /**
     * Parses the given JSON object into a drone object of type {@code T}.
     *
     * @param obj The JSON object containing drone data.
     * @return A drone object of type {@code T} parsed from the JSON data.
     */
    T parse(JSONObject obj);

    /**
     * Validates whether the given JSON object contains the required data for a drone.
     *
     * @param obj The JSON object to validate.
     * @return {@code true} if the JSON object contains the necessary fields, otherwise {@code false}.
     */
    boolean isValid(JSONObject obj);

    /**
     * Returns the API endpoint belonging to this parser.
     *
     * @return A {@link String} Link to the API endpoint.
     */
    String getEndpoint();
}