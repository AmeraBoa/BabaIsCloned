package baba.utils;

import baba.level.LevelManager;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;

/**
 * Handle the input.
 */
public final class InputComponent {
    private static ApplicationContext context;
    private static Direction MoveDirection;

    private InputComponent() {}

    public static void init(ApplicationContext context) {
        InputComponent.context = context;
    }

    /**
     * Return the direction of the move input. Default value is set to NONE.
     * @return the direction of the move input.
     * @see Direction
     */
    public static Direction getMoveDirection() {
        return MoveDirection;
    }

    /**
     * Process receiving inputs and controls.
     */
    public static void processInput() {
        MoveDirection = Direction.NONE;
        Event event = context.pollOrWaitEvent(30);
        if(event == null) return;

        Event.Action action = event.getAction();
        if(action == Event.Action.KEY_RELEASED) {
            MoveDirection = getMoveInputToDirection(event);
            var levelManager = LevelManager.getInstance();
            switch (event.getKey()) {
                case R -> levelManager.reload();
                case D -> levelManager.previous();
                case F -> levelManager.skip();
                case Q -> levelManager.quit();
                case S -> levelManager.save();
                case T -> levelManager.load();
            }
        }
    }

    /**
     * Convert the input move key to direction.
     *
     * @param event the current event of the application.
     * @return the direction corresponding to the move input.
     * @see Direction
     */
    private static Direction getMoveInputToDirection(Event event) {
        return switch(event.getKey()) {
            case RIGHT -> Direction.RIGHT;
            case LEFT -> Direction.LEFT;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            default -> Direction.NONE;
        };
    }
}
