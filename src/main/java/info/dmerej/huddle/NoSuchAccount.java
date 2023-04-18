package info.dmerej.huddle;

public class NoSuchAccount extends RuntimeException {
    public NoSuchAccount(String message) {
        super(message);
    }
}
