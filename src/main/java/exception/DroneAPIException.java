package exception;

/**
 * The DroneAPIException class is responsible for a custom exception that happens when calling the API.
 */
public class DroneAPIException extends RuntimeException {
    public DroneAPIException(String message) {
        super(message);
    }
}