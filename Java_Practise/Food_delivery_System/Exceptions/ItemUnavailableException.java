package Food_delivery_System.Exceptions;

public class ItemUnavailableException extends RuntimeException {

    public ItemUnavailableException() {
        super();
    }

    public ItemUnavailableException(String message) {
        super(message);
    }

    public ItemUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemUnavailableException(Throwable cause) {
        super(cause);
    }
}