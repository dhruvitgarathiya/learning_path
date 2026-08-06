package Food_delivery_System.Exceptions;

public class InvalidCouponException extends RuntimeException {

    public InvalidCouponException() {
        super();
    }

    public InvalidCouponException(String message) {
        super(message);
    }

    public InvalidCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCouponException(Throwable cause) {
        super(cause);
    }
}