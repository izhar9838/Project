package sm.central.exception.custom;

public class MarksAlreadyExistException extends RuntimeException {
    public MarksAlreadyExistException(String message) {
        super(message);
    }
}
