package Game.GameHandler;

public class InputHandler {
    private static InputHandler ourInstance = new InputHandler();

    public static InputHandler getInstance() {
        return ourInstance;
    }

    private InputHandler() {
    }

}
