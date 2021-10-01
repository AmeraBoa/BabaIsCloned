package baba.block;

import baba.block.identifiers.*;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Identifier of a block.
 */
public interface BlockID {

    // Allow conversion between String and BlockID.
    HashMap<String, BlockID> stringToID = new HashMap<>();

    /**
     * Return the block identifier of a specified String.
     * @param token the string to be tested.
     * @return the corresponding block identifier.
     */
    static BlockID valueOf(String token) {
        if (token == null) return null;

        return stringToID.get(token);
    }

    /**
     * Initialize a map of string to identifier.
     */
    static void init() {
        Arrays.asList(Noun.values()).forEach(instance -> stringToID.put(instance.toString(), instance));
        Arrays.asList(Operator.values()).forEach(instance -> stringToID.put(instance.toString(), instance));
        Arrays.asList(Property.values()).forEach(instance -> stringToID.put(instance.toString(), instance));
        Arrays.asList(RealObject.values()).forEach(instance -> stringToID.put(instance.toString(), instance));
        Arrays.asList(Group.values()).forEach(instance -> stringToID.put(instance.toString(), instance));
    }
}
