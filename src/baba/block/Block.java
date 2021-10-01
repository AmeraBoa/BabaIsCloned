package baba.block;

import baba.block.identifiers.Property;
import baba.block.identifiers.RealObject;
import baba.utils.SaveLoader;
import baba.utils.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;

/**
 * Element of a level.
 */
public class Block {
    private BlockID identifier;
    private Image image;
    private Vector2 position;
    private boolean dead = false;
    private final HashSet<Property> properties = new HashSet<>();

    public Block(BlockID identifier) {
        this.identifier = identifier;
        this.image = new ImageIcon("res/sprites/" + identifier + ".gif").getImage();
    }

    /**
     * Return the position of this block.
     * @return the position of this block.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set this block position.
     * @param position the position to
     */
    public void setPosition(Vector2 position) {
        this.position = new Vector2(position.x(), position.y());
    }

    /**
     * Set this block state.
     * @param state the state of the block.
     */
    public void setDead(boolean state) {
        dead = state;
    }

    /**
     * Return true if this block is dead.
     * @return true if this block is dead.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Return the identifier of this block.
     * @return the identifier of this block.
     */
    public BlockID getID() {
        return this.identifier;
    }

    /**
     * Return the image of this block.
     * @return the image of this block.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Return the priority in the layering. Used when rendering blocks.
     * @return the priority of the block.
     */
    public int getPriority() {
        if(identifier instanceof RealObject) return -((RealObject) identifier).ordinal();
        return 1;
    }

    /**
     * Compare this block to the specified object.
     * @param o the object to compare this block against.
     * @return true if the given object represent a block and has the same identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block block)) return false;
        return identifier.equals(block.identifier);
    }

    /**
     * Return a hashcode for this object.
     * @return a hashcode for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    /**
     * Copy the given block attributes.
     * @param block the block attributes to copy from.
     */
    public void copy(Block block) {
        this.identifier = block.identifier;
        this.image = block.image;
        this.dead = block.dead;
    }

    public String toTextFormat() {
        return identifier.toString() + SaveLoader.SEPARATOR + position.x() +
                SaveLoader.SEPARATOR + position.y() + SaveLoader.SEPARATOR + dead;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public boolean hasProperty(Property property) {
        return properties.contains(property);
    }

    public void clearProperties() {
        properties.clear();
    }
}
