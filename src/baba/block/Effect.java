package baba.block;

import baba.block.identifiers.Noun;
import baba.block.identifiers.Property;
import baba.level.LevelManager;
import baba.utils.InputComponent;

/**
 * An effect is applied to a block modifying the block itself or the game state.
 */
public interface Effect {
    /**
     * Apply the property effect on the specified block.
     */
    void apply(Block block);

    class Defeat implements Effect {
        @Override
        public void apply(Block block) {
            var currentLevel = LevelManager.getInstance().getCurrentLevel();
            var blockAt = currentLevel.findByPosition(block.getPosition());

            blockAt.stream().filter(blk -> blk.hasProperty(Property.YOU))
                    .forEach(blk -> blk.setDead(true));
        }
    }

    class Hot implements Effect {
        @Override
        public void apply(Block block) {
            var currentLevel = LevelManager.getInstance().getCurrentLevel();
            var blockAt = currentLevel.findByPosition(block.getPosition());
            for (Block blk:blockAt) {
                if(blk.hasProperty(Property.MELT)) {
                    blk.setDead(true);
                }
            }
        }
    }

    class Sink implements Effect {
        @Override
        public void apply(Block block) {
            var currentLevel = LevelManager.getInstance().getCurrentLevel();

            var blockAt = currentLevel.findByPosition(block.getPosition());
            if(blockAt.stream().anyMatch(blk -> !blk.equals(block))) {
                blockAt.forEach(blk -> blk.setDead(true));
                block.setDead(true);
            }
        }
    }

    class Win implements Effect {
        @Override
        public void apply(Block block) {
            var currentLevel = LevelManager.getInstance().getCurrentLevel();
            var blockAt = currentLevel.findByPosition(block.getPosition());

            if(blockAt.stream().anyMatch(blk -> blk.hasProperty(Property.YOU))) {
                currentLevel.setFinished(true);
                System.out.println("Congratulation !");
            }
        }
    }

    class You implements Effect {
        public void apply(Block block) {
            var direction = InputComponent.getMoveDirection();

            var currentLevel = LevelManager.getInstance().getCurrentLevel();
            currentLevel.move(block, direction);
        }
    }

    class Real implements Effect {
        public void apply(Block block) {
            if(block.getID() instanceof Noun noun) {
                var linkedObj = noun.getLinkedObject();
                block.copy(new Block(linkedObj));
            }
        }
    }
}
