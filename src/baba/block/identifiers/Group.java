package baba.block.identifiers;

import baba.block.BlockID;
import baba.block.Text;

import java.util.HashSet;
import java.util.function.Predicate;

/**
 * Group of block identifier, requires a condition to test belongingness.
 */
public enum Group implements BlockID, Text {
    TEXT(item -> item instanceof Text),
    NOUN(item -> item instanceof Noun),
    OPERATOR(item -> item instanceof Operator),
    PROPERTY(item -> item instanceof Property);

    private final Predicate<BlockID> condition;

    Group(Predicate<BlockID> condition) {
        this.condition = condition;
    }

    public HashSet<Text> getElements() {
        var elements = new HashSet<Text>();
        stringToID.forEach((s, id) -> {
            if (condition.test(id) && !(id instanceof Group))
                elements.add((Text) id);
        });
        return elements;
    }
}
