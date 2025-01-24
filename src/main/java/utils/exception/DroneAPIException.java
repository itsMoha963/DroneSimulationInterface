package utils.exception;

public class DroneAPIException extends RuntimeException {
    public DroneAPIException(String message) {
        super(message);
    }
    public DroneAPIException(String message, Throwable cause) { super(message, cause); }
}
