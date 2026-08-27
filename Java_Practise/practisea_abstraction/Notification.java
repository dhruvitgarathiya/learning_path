public abstract class Notification {
    protected String recipient;
    protected String message;

    public Notification(String recipient, String message){
        this.recipient = recipient;
        this.message = message;
    }

    public abstract void send();
    public abstract void validate();
    public abstract void log();
}
