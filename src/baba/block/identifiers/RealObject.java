package baba.block.identifiers;

import baba.block.BlockID;

/**
 * A real object identifier.
 */
public enum RealObject implements BlockID {
    BABA,
    ROCK,
    WALL,
    FLAG,
    LAVA,
    WATER,
    SKULL,
    TILE,
    GRASS;

    @Override
    public String toString() {
        return "OBJ_" + super.toString();
    }
}
