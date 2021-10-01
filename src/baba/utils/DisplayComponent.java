package baba.utils;

import baba.block.Block;
import baba.level.Level;
import baba.level.LevelManager;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

import java.awt.*;

/**
 * Allow rendering of the game elements.
 */
public final class DisplayComponent {
    private static ApplicationContext context;
    private static Vector2 screenSize;
    private static int blockSize; // The size of a block side in pixels.
    private static Vector2 offsetCenter;

    private DisplayComponent() {}

    public static void init(ApplicationContext context) {
        DisplayComponent.context = context;
        ScreenInfo screenInfo = context.getScreenInfo();
        DisplayComponent.screenSize = new Vector2((int) screenInfo.getWidth(), (int) screenInfo.getHeight());
        initBlockSize();
    }

    /**
     * Set the blocksize based on screen dimension.
     */
    private static void initBlockSize() {
        var maxLevelRows = 19;
        var maxLevelColumns = 25;
        blockSize = DisplayComponent.getScreenSize().y() < DisplayComponent.getScreenSize().x() ?
                  DisplayComponent.getScreenSize().y() / maxLevelRows
                : DisplayComponent.getScreenSize().x() / maxLevelColumns;
    }

    /**
     * Return the size of the current window.
     * @return the size of the current window.
     */
    public static Vector2 getScreenSize() {
        return screenSize;
    }

    /**
     *  Set the offset of the level so that the rendering is centered.
     *
     * @param levelSize the size of the current level.
     */
    public static void initOffsetCenter(Vector2 levelSize) {
        offsetCenter = new Vector2(
                (screenSize.x() - levelSize.x() * blockSize) / 2,
                (screenSize.y() - levelSize.y() * blockSize) / 2
        );
    }

    /**
     * Render the game.
     */
    public static void render() {
        context.renderFrame(graphics -> {
            clear(graphics);
            draw(graphics, LevelManager.getInstance().getCurrentLevel());
        });
    }

    /**
     * Clear the window.
     * @param graphics the graphics to be cleared.
     */
    private static void clear(Graphics2D graphics) {
        graphics.clearRect(0, 0, getScreenSize().x(), getScreenSize().y());
    }

    /**
     * Draw a level.
     * @param graphics the graphics to draw on.
     * @param level the level to be drawn.
     * @see Level
     */
    private static void draw(Graphics2D graphics, Level level) {
        graphics.setColor(new Color(9, 9, 9));
        graphics.fillRect(
                offsetCenter.x(), offsetCenter.y(),
                blockSize * level.getSize().x(), blockSize * level.getSize().y()
        );
        level.getElements().forEach(elem -> draw(graphics, elem));
    }

    /**
     * Draw a block.
     * @param graphics the graphics to draw on.
     * @param block the block to be drawn.
     * @see Block
     */
    private static void draw(Graphics2D graphics, Block block) {
        var position = block.getPosition();
        var fitToScreenPosition = position.mul(blockSize).add(offsetCenter);
        graphics.drawImage(
                block.getImage(),
                fitToScreenPosition.x(), fitToScreenPosition.y(),
                blockSize, blockSize,
                new Color(0, 0, 0, 0), null
        );
    }
}
