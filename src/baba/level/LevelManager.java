package baba.level;

import baba.block.BlockID;
import baba.rules.Rule;
import baba.utils.DisplayComponent;
import baba.utils.InputComponent;
import baba.utils.SaveLoader;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Singleton class to handle level chaining and basic level commands.
 */
public final class LevelManager {
    private static final LevelManager instance = new LevelManager();

    private ApplicationContext context;
    private List<File> levelFiles = null;
    private Level currentLevel = null;
    private File currentLevelFile;
    private int index;

    private final List<Rule> startingRule = new ArrayList<>();

    /**
     * Return this level manager instance.
     * @return this level manager instance.
     */
    public static LevelManager getInstance() {
        return instance;
    }

    private LevelManager() {
        Application.run(new Color(21, 25, 32), this::init);
    }

    /**
     * Initialize the application context.
     * @param context the context provided by the library.
     */
    private void init(ApplicationContext context) {
        this.context = context;
        DisplayComponent.init(context);
        InputComponent.init(context);
        BlockID.init();
    }

    /**
     * Return the current level played.
     * @return the current level played.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Return the current level name.
     * @return the name of the current level.
     */
    public String getCurrentLevelName() {
        return currentLevelFile.getName();
    }

    /**
     * Start the game.
     */
    public void start() {
        for (index = 0; index < levelFiles.size(); index++) {
            startLevel(levelFiles.get(index));
        }

        quit();
    }

    /**
     * Set the default level folder. Levels are sorted by name and played one after another.
     * @param folder the folder of the levels.
     */
    public void setStartingFolder(String folder) {
        levelFiles = Arrays.stream(
                Objects.requireNonNull(new File(folder).listFiles()))
                .sorted().collect(Collectors.toList()
        );
    }

    /**
     * Set a single starting level. The game stop after the level is finished.
     * @param file the name of the level file.
     */
    public void setStartingFile(String file) {
        levelFiles = List.of(new File(file));
    }

    /**
     * Add a new rule to the starting rules.
     * @param rule the rule to add.
     */
    public void addToStartingRule(Rule rule) {
        startingRule.add(rule);
    }

    public List<Rule> getStartingRule() {
        return startingRule;
    }

    /**
     * Start a level.
     * @param file the level file, in this case the current level to be played.
     */
    public void startLevel(File file) {
        currentLevel = new Level(file);
        currentLevelFile = file;
        while(!currentLevel.isFinished()) {
            InputComponent.processInput();
            currentLevel.update();
            DisplayComponent.render();
        }
    }

    /**
     * Go to the previous level.
     */
    public void previous() {
        if(index != 0) {
            index -= 2;
            skip();
        }
    }

    /**
     * Skip to the next level.
     */
    public void skip() {
        currentLevel.setFinished(true);
    }

    /**
     * Quit the game.
     */
    public void quit() {
        context.exit(0);
    }

    /**
     * Reload the current level.
     */
    public void reload() {
        currentLevel = new Level(currentLevelFile);
    }

    /**
     * Save the current game state.
     */
    public void save() {
        new SaveLoader().save(currentLevel);
    }

    /**
     * Load a game state from res/saveFile.sav.
     */
    public void load() {
        new SaveLoader().load(currentLevel);
    }

    /**
     * Skip to the specified level.
     * @param levelName the name of the level to start.
     */
    public void skipAtLevel(String levelName) {
        if(currentLevelFile.getName().equals(levelName)) return;

        for (int i = 0; i < levelFiles.size(); i++) {
            if(levelFiles.get(i).getName().equals(levelName)) {
                index = i - 1;
                skip();
            }
        }
    }
}
