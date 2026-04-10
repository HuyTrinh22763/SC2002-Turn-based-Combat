package boundary;

public class QuitGameException extends RuntimeException {
    public QuitGameException() {
        super("Game exited early by the user.");
    }
}
