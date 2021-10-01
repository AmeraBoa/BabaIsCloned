package baba.rules;

import baba.block.*;
import baba.block.identifiers.Group;
import baba.block.identifiers.Noun;
import baba.block.identifiers.Property;
import baba.block.identifiers.RealObject;
import baba.level.LevelManager;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Interface for implementing evaluation methods.
 */
public interface Operation {
    /**
     * Evaluate an operation between two value and return the result.
     * @param leftOperand the left value of the operation.
     * @param rightOperand the right value of the operation.
     * @return the result of the operation.
     */
    Text eval(Text leftOperand, Text rightOperand);

    /**
     * Check the dead blocks with the same identifier as the left text element
     * and create a new one based on the right element.
     */
    class Has implements Operation {
        @Override
        public Text eval(Text left, Text right) {
            if(!isOperandValid(left, right))
                return right;

            var level = LevelManager.getInstance().getCurrentLevel();

            BlockID realObj = null;
            if(left instanceof Noun noun)
                realObj = noun.getLinkedObject();
            var dead = level.getDead();

            for(Block block:dead) {
                RealObject deadTag = (RealObject) block.getID();
                if(realObj == deadTag) {
                    block.copy(new Block(((Noun) right).getLinkedObject()));
                }
            }

            
            return right;
        }

        private boolean isOperandValid(Text leftOperand, Text rightOperand) {
            return (leftOperand instanceof Noun && rightOperand instanceof Noun);
        }
    }

    /**
     * Set the left block corresponding real object based on the type of the right block.
     * If the right block is a property update the left block properties.
     * If it is a noun, the attributes in the left block are set to this block corresponding attributes.
     */
    class Is implements Operation {
        @Override
        public Text eval(Text left, Text right) {
            if(!isOperandValid(left, right))
                return right;

            if(left instanceof Group group) {
                for (BlockID block:group.getElements()) {
                    if(right instanceof Noun)
                        evalForNoun(block, right);
                    else
                        evalForProperty(block, right);
                }
            }

            BlockID leftLinkedObj = left;
            if(left instanceof Noun noun && right != Property.REAL)
                leftLinkedObj = noun.getLinkedObject();

            if(right instanceof Noun) {
                evalForNoun(leftLinkedObj, ((Noun) right).getLinkedObject());
            }
            else {
                evalForProperty(leftLinkedObj, right);
            }

            return right;
        }

        private boolean isOperandValid(Text left, Text right) {
            return (left instanceof Group || left instanceof Noun)
                    && (right instanceof Noun || right instanceof Property);
        }

        private void evalForNoun(BlockID left, BlockID right) {
            var currentLevel = LevelManager.getInstance().getCurrentLevel();

            currentLevel.findByID(left).forEach(block -> {
                block.copy(new Block(right));
            });
        }

        private void evalForProperty(BlockID left, BlockID right) {
            var currentLevel = LevelManager.getInstance().getCurrentLevel();

            currentLevel.addPropertyToAll(left, (Property) right);
        }
    }

    /**
     * Filter the left operand when the corresponding left block is on the
     * same position as the one contained in the right.
     */
    class On implements Operation {
        @Override
        public Text eval(Text left, Text right) {
            if(!(left instanceof Noun) || !(right instanceof Noun)) return right;

            var level = LevelManager.getInstance().getCurrentLevel();
            BlockID leftRealObj;
            BlockID rightRealObj;
            leftRealObj = ((Noun) left).getLinkedObject();
            rightRealObj = ((Noun) right).getLinkedObject();

            var leftTagPos = level.findByID(leftRealObj).stream().map(Block::getPosition).collect(Collectors.toList());
            var rightTagPos = level.findByID(rightRealObj).stream().map(Block::getPosition).collect(Collectors.toList());

            if(!Collections.disjoint(leftTagPos, rightTagPos)) return left;

            return right;
        }
    }
}
