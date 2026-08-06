package Food_delivery_System.Exceptions;

public class RestaurantClosedException extends RuntimeException {

    public RestaurantClosedException() {
        super();
    }

    public RestaurantClosedException(String message) {
        super(message);
    }

    public RestaurantClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestaurantClosedException(Throwable cause) {
        super(cause);
    }
}