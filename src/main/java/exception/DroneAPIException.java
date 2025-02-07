package exception;

/**
 * The {@code DroneAPIException} class is custom exception that is used for the drone API.
 * This exception extends {@link RuntimeException}.
 */
public class DroneAPIException extends RuntimeException {

    /**
     * @param message The error message describing the API exception.
     */
    public DroneAPIException(String message) {
        super(message);
    }
}