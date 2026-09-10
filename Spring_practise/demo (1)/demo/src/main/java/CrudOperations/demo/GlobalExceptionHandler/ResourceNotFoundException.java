package CrudOperations.demo.GlobalExceptionHandler;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String Message){
        super(Message);
    }
}
