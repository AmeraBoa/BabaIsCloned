package baba.level;

import baba.block.Block;
import baba.block.BlockID;
import baba.block.identifiers.Property;
import baba.block.Text;
import baba.rules.Parser;
import baba.rules.Rule;
import baba.utils.Direction;
import baba.utils.DisplayComponent;
import baba.utils.Vector2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A game level, containing rules and blocks.
 */
public class Level {
    private BlockMover mover;
    private final List<Block> elements = new ArrayList<>();
    private final Rules rules = new Rules();
    private boolean finished = false;

    public Level(File file) {
        try {
            load(Objects.requireNonNull(file));
        } catch (IOException e) {
            System.out.println("this file is invalid");
        }
    }

    /**
     * Load the specified file as a level tilemap. Initialize this level based on the file content.
     * @param file the level file to load.
     */
    private void load(File file) throws IOException {
        try(var reader = new BufferedReader(new FileReader(file))) {
            initSize(reader);
            fillBoard(reader);
        }
    }

    /**
     * Set this level size based on the information provided by the reader.
     * @param reader the reader of a level file.
     */
    private void initSize(BufferedReader reader) throws IOException {
        var line = reader.readLine().split(",");

        // The first line contains the level dimensions.
        var columns = Integer.parseInt(line[0]);
        var rows = Integer.parseInt(line[1]);
        this.mover = new BlockMover(new Vector2(columns, rows));
        DisplayComponent.initOffsetCenter(new Vector2(columns, rows));
    }

    /**
     * Add blocks to the Board based on the reader content.
     * @param reader the reader of a level file.
     * @see BlockMover
     */
    private void fillBoard(BufferedReader reader) throws IOException {
        for (int j = 0; reader.ready(); j++) {
            var line = reader.readLine().split(",");

            for(int i = 0; i < line.length; i++) {
                var token = line[i];
                if(token == null) continue;

                var tag = BlockID.valueOf(token);
                if(tag != null && !mover.notInBound(new Vector2(i, j))) {
                    var block = new Block(tag);
                    block.setPosition(new Vector2(i, j));
                    mover.add(block);
                }
            }
        }
    }

    /**
     * Return true if this level is finished.
     * @return true if this level is finished.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Set the level current state; finished or not.
     * @param state the state of the level.
     */
    public void setFinished(boolean state) {
        this.finished = state;
    }

    /**
     * Return the size of this level.
     * @return the size of this level.
     */
    public Vector2 getSize() {
        return mover.getSize();
    }

    /**
     * Update the game state.
     */
    public void update() {
        applyProperties();
        if(mover.isChanged()) {
            clearProperties();
            rules.update();
            mover.setChanged(false);
        }
    }

    /**
     * Rule recognition class.
     */
    private class Rules {
        private final List<Rule> defaultRules = LevelManager.getInstance().getStartingRule();
        private final List<Rule> rules = new ArrayList<>();
        private static final int RULE_MIN_SIZE = 3;

        /**
         * Identify the rule on the board and apply them.
         */
        private void update() {
            var expressionParser = new Parser();
            Text[][] textBoard = new Text[getSize().x()][getSize().y()];

            rules.clear();

            fillWithTextBlocks(textBoard);
            addRuleFromColumns(textBoard);
            addRuleFromLines(textBoard);

            defaultRules.forEach(r -> rules.add(r.clone()));
            expressionParser.parse(rules);
        }

        /**
         * File a given two dimension array with the textual Block name.
         * @param textBoard the board representation containing text elements only.
         */
        private void fillWithTextBlocks (Text[][]textBoard){
            getElements().stream().filter(item -> item.getID() instanceof Text)
                    .forEach(block -> textBoard[block.getPosition().x()][block.getPosition().y()] = (Text) block.getID());
        }

        /**
         * Add rule based on the lines in the given text board.
         * @param textBoard the board representation containing text elements only.
         */
        private void addRuleFromLines(Text[][] textBoard) {
            var rule = new ArrayList<Text>();
            for (int j = 0; j < getSize().y(); j++) {
                for (int i = 0; i < getSize().x(); i++) {
                    addTextToRule(textBoard[i][j], rule);
                }
            }
        }

        /**
         * Add rule based on the columns in the given text board.
         * @param textBoard the board representation containing text elements only.
         */
        private void addRuleFromColumns(Text[][] textBoard) {
            var rule = new ArrayList<Text>();
            for (int i = 0; i < getSize().x(); i++) {
                for (int j = 0; j < getSize().y(); j++) {
                    addTextToRule(textBoard[i][j], rule);
                }
            }
        }

        /**
         * Decide if the specified text elements should be added to the rule.
         * @param text the text elements to add.
         * @param rule the rule where the text need to be added.
         */
        private void addTextToRule(Text text, ArrayList<Text> rule) {
            if(text == null) {
                if(rule.size() >= RULE_MIN_SIZE) {
                    rules.add(new Rule(rule));
                }
                if(!rule.isEmpty()) {
                    rule.clear();
                }
            }
            else {
                rule.add(text);
            }
        }
    }

    /**
     * Set of methods for moving and pushing blocks.
     */
    private class BlockMover {
        private final Vector2 size;
        private boolean changed = true;

        public BlockMover(Vector2 size) {
            this.size = size;
        }

        /**
         * Set the board state.
         * @param state the state of the board.
         */
        public void setChanged(boolean state) {
            changed = state;
        }

        /**
         * Return true if this board has been changed.
         * @return true if this board has been changed.
         */
        public boolean isChanged() {
            return changed;
        }

        /**
         * Return the size of this board.
         * @return the size of this board.
         */
        public Vector2 getSize() {
            return size;
        }

        /**
         * Add a block to this board.
         * @param block the block to be added.
         */
        public void add(Block block) {
            elements.add(block);
        }

        /**
         * Return true if the given position is not in the board.
         * @param position the position to be checked.
         */
        public boolean notInBound(Vector2 position) {
            return position.x() < 0 || position.x() >= size.x() ||
                    position.y() < 0 || position.y() >= size.y();
        }

        /**
         * Move a block in the context of the board.
         * @param block the block to be moved.
         * @param direction the direction in which the block is to be moved.
         */
        private void moveInContext(Block block, Direction direction) {
            if(direction == Direction.NONE || block == null) return;

            if(isValidMove(block, direction)) {
                push(block, direction);
                move(block, direction);
                changed = true;
            }
        }

        /**
         * Return true if a block can move in a specified direction.
         * @param block the block to be moved.
         * @param direction the direction in which the block is to be moved.
         */
        private boolean isValidMove(Block block, Direction direction) {
            var nextPos = block.getPosition().add(direction);
            if(notInBound(nextPos)) return false;

            var nextBlocks = findByPosition(nextPos);
            if(nextBlocks.isEmpty()) return true;

            if(nextBlocks.stream().anyMatch(blk -> blk.hasProperty(Property.STOP)))
                return false;

            if(nextBlocks.stream().noneMatch(blk -> blk.hasProperty(Property.PUSH)))
                return true;

            return nextBlocks.stream().allMatch(item -> isValidMove(item, direction));
        }

        /**
         * Push a block in a specified direction.
         * @param block the block to be pushed.
         * @param direction the direction in which the blocks is to be pushed.
         */
        private void push(Block block, Direction direction) {
            var nextPos = block.getPosition().add(direction);
            var pushBlocks = findByPosition(nextPos).stream()
                    .filter(item -> item.hasProperty(Property.PUSH) && !item.hasProperty(Property.YOU))
                    .collect(Collectors.toList());

            pushBlocks.forEach(item -> moveInContext(item, direction));
        }

        /**
         * Move a block to a specified position.
         * @param block the block to be moved.
         * @param direction the destination of the block.
         */
        private void move(Block block, Direction direction) {
            var nextPos = block.getPosition().add(direction);
            block.setPosition(nextPos);
        }
    }

    /**
     * Return all the blocks still alive.
     * @return the list of Block of blocks which are not dead.
     */
    public List<Block> getElements() {
        return elements.stream().sorted(Comparator.comparingInt(Block::getPriority))
                .filter(Predicate.not(Block::isDead)).collect(Collectors.toList());
    }

    /**
     * Return the dead blocks.
     * @return the list of blocks which are dead.
     */
    public List<Block> getDead() {
        return elements.stream().filter(Block::isDead).collect(Collectors.toList());
    }

    /**
     * Find a block based on a given position.
     * @param position the position of the blocks.
     * @return the list of block with the given position.
     */
    public List<Block> findByPosition(Vector2 position) {
        return getElements().stream().filter(block -> block.getPosition().equals(position)).collect(Collectors.toList());
    }

    /**
     * Find a block based on a given identifier.
     * @param id the identifier to match.
     * @return  the list of block with the given identifier.
     */
    public List<Block> findByID(BlockID id) {
        return getElements().stream().filter(block -> block.getID().equals(id)).collect(Collectors.toList());
    }

    /**
     * Find a block based on a given property.
     * @param property the property to match.
     * @return  the list of block with the given property.
     */
    public List<Block> findByProperty(Property property) {
        return getElements().stream().filter(block -> block.hasProperty(property)).collect(Collectors.toList());
    }

    /**
     * Move a block in the specified direction.
     * @param block the block to be moved.
     * @param direction the direction in which the block is to be moved.
     */
    public void move(Block block, Direction direction) {
        mover.moveInContext(block, direction);
    }

    /**
     * Load the specified elements, replacing the current blocks.
     * @param elements the elements to load.
     */
    public void loadElements(List<Block> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
    }

    /**
     * Add a property to all the block in this level with the specified identifier.
     * @param identifier the identifier of the block.
     * @param property the property to add.
     */
    public void addPropertyToAll(BlockID identifier, Property property) {
        getElements().stream().filter(block -> block.getID() == identifier)
                .forEach(block -> block.addProperty(property));
    }

    /**
     * Clear the properties in this level blocks.
     */
    public void clearProperties() {
        getElements().forEach(Block::clearProperties);
    }

    /**
     * Apply the properties on this level blocks based on their order in the enum Property.
     */
    public void applyProperties() {
        var properties = Property.values();

        for (Property property : properties) {
            var blocks = findByProperty(property);
            var effect = property.getEffect();

            if(effect != null) {
                blocks.forEach(effect::apply);
            }
        }
    }
}
