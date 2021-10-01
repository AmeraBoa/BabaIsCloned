package baba.utils;

/**
 * A two dimension vector of integer.
 */
public record Vector2(int x, int y) {
    /**
     * Multiply this vector by the given value.
     *
     * @param   scalar the value by which the vector should be multiplied.
     * @return  the result vector of the multiplication.
     */
    public Vector2 mul(int scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    /**
     * Add this vector to another.
     *
     * @param v the vector to be added.
     * @return  the result vector of the addition.
     */
    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    /**
     * Add a specified direction to this vector.
     *
     * @param direction the direction to be added.
     * @return  the result vector of the addition.
     * @see Direction
     */
    public Vector2 add(Direction direction) {
        return this.add(direction.toVector2());
    }
}
