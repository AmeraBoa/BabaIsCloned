package baba.block.identifiers;

import baba.block.BlockID;
import baba.block.Text;
import baba.rules.Operation;

/**
 * The operator identifier can define an operation which will be applied when the parsing occurs.
 */
public enum Operator implements BlockID, Text {
    AND(null),
    ON(new Operation.On()),
    HAS(new Operation.Has()),
    IS(new Operation.Is());


    private final Operation operation;

    Operator(Operation operation) {
        this.operation = operation;
    }

    /**
     * Return the operation of this operator.
     *
     * @return the operation of this operator.
     * @see Operation
     */
    public Operation getOperation() {
        return operation;
    }

}
