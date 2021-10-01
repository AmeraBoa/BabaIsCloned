package baba.utils;

/**
 * Represent a direction.
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE;

    /**
     * Convert this direction to a Vector2.
     * @return the Vector2 corresponding to the direction.
     * @see Vector2
     */
    public Vector2 toVector2() {
        return switch (this) {
            case LEFT -> new Vector2(-1, 0);
            case RIGHT -> new Vector2(1, 0);
            case UP -> new Vector2(0, -1);
            case DOWN -> new Vector2(0, 1);
            case NONE -> new Vector2(0, 0);
        };
    }
}
