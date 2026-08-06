package Food_delivery_System.Exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super();
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientBalanceException(Throwable cause) {
        super(cause);
    }
}