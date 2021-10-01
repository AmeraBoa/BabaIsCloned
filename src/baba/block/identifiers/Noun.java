package baba.block.identifiers;

import baba.block.BlockID;
import baba.block.Text;

import java.util.Objects;

/**
 * A noun identifier.
 */
public enum Noun implements BlockID, Text {
    BABA(RealObject.BABA),
    ROCK(RealObject.ROCK),
    WALL(RealObject.WALL),
    FLAG(RealObject.FLAG),
    LAVA(RealObject.LAVA),
    WATER(RealObject.WATER),
    SKULL(RealObject.SKULL),
    TILE(RealObject.TILE),
    GRASS(RealObject.GRASS);

    private final RealObject linkedObject;

    Noun(RealObject linkedObject) {
        this.linkedObject = Objects.requireNonNull(linkedObject);
    }

    /**
     * Return the real object corresponding to this noun.
     *
     * @return the real object corresponding to this noun.
     * @see RealObject
     */
    public RealObject getLinkedObject() {
        return linkedObject;
    }
}
