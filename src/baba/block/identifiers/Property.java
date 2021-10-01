package baba.block.identifiers;

import baba.block.BlockID;
import baba.block.Effect;
import baba.block.Text;

/**
 * A property identifier which contain an effect.
 *
 * @see Effect
 */
public enum Property implements BlockID, Text {
    YOU(new Effect.You()),
    PUSH(null),
    STOP(null),
    HOT(new Effect.Hot()),
    MELT(null),
    SINK(new Effect.Sink()),
    DEFEAT(new Effect.Defeat()),
    WIN(new Effect.Win()),
    REAL(new Effect.Real());


    private final Effect effect;

    Property(Effect effect) {
        this.effect = effect;
    }

    /**
     * Return the effect of this property.
     *
     * @return the effect of this property.
     * @see Effect
     */
    public Effect getEffect() {
        return effect;
    }
}
