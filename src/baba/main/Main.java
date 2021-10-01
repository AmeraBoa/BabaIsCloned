package baba.main;

import baba.level.LevelManager;
import baba.rules.Rule;

import java.util.function.Function;

public final class Main {
    private Main() {}

    private static void setDefaultSettings() {
        LevelManager.getInstance().setStartingFolder("res/levels");
        LevelManager.getInstance().addToStartingRule(new Rule("TEXT", "IS", "PUSH"));
    }

    /**
     * parse the arguments and set the game settings accordingly.
     * @param args the arguments to parse.
     */
    private static void parseArguments(String... args) {
        for (int i = 2; i < args.length; i++) {
            switch (args[i]) {
                case "--execute" -> {
                    if (args.length - i <= 3) {
                        throw new IllegalArgumentException("Illegal number of argument");
                    }
                    LevelManager.getInstance().addToStartingRule(new Rule(args[i + 1], args[i + 2], args[i + 3]));
                    i += 3;
                }
                case "--level" -> {
                    if (args.length - i <= 1) {
                        throw new IllegalArgumentException("command require a file path");
                    }
                    LevelManager.getInstance().setStartingFile(args[i + 1]);
                    i++;
                }
                case "--levels" -> {
                    if (args.length - i <= 1) {
                        throw new IllegalArgumentException("command require a folder path");
                    }
                    LevelManager.getInstance().setStartingFolder(args[i + 1]);
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) {
        setDefaultSettings();
        parseArguments(args);
        LevelManager.getInstance().start();
    }
}