package baba.utils;

import baba.block.Block;
import baba.block.BlockID;
import baba.level.Level;
import baba.level.LevelManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Allow saving and loading game state.
 */
public class SaveLoader {
    private final static Path SAVEFILEPATH = Path.of("res/saveFile.sav");
    public final static String SEPARATOR = " ";

    /**
     * Save the level elements to file.
     * @param level the level to be saved.
     */
    public void save(Level level) {
        System.out.println("Game saved at " + SAVEFILEPATH + "...");
        try(var writer = Files.newBufferedWriter(SAVEFILEPATH,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE)) {
            levelToText(level, writer);
        }catch (IOException err) {
            System.out.println("Error while saving...");
        }
    }

    /**
     * Write the specified level to the save file in a text format.
     * @param level the level to be saved.
     * @param writer the writer to the save file.
     */
    private void levelToText(Level level, BufferedWriter writer) throws IOException {
        var elements = level.getElements();

        writer.write(LevelManager.getInstance().getCurrentLevelName() + "\n");
        for (Block elem : elements) {
            writer.write(elem.toTextFormat() + "\n");
        }
        writer.write("\n");
    }

    /**
     * Read the save file and update the specified level.
     * @param level the level that to be updated.
     */
    public void load(Level level) {
        if(!Files.exists(SAVEFILEPATH)) {
            System.out.println(SAVEFILEPATH + " is missing...");
            return;
        }

        System.out.println("Game loaded from " + SAVEFILEPATH + "...");
        try(var reader = Files.newBufferedReader(SAVEFILEPATH)) {
            levelFromText(level, reader);
        } catch (IOException err) {
            System.out.println("Error while loading, file is invalid");
        }
    }

    /**
     * Create a list of block based on the save file content and send it to the specified level.
     * @param level the level to receive the modification.
     * @param reader the reader to the save file.
     */
    private void levelFromText(Level level, BufferedReader reader) throws IOException {
        var elements = new ArrayList<Block>();

        LevelManager.getInstance().skipAtLevel(reader.readLine());

        do {
            var line = reader.readLine().split(SEPARATOR);

            if(line.length <= 1) break; // End of file.

            var id = BlockID.valueOf(line[0]);
            var position = new Vector2(Integer.parseInt(line[1]), Integer.parseInt(line[2]));
            var dead = Boolean.parseBoolean(line[3]);

            var block = new Block(id);
            block.setPosition(position);
            block.setDead(dead);

            elements.add(block);
        }while(reader.ready());

        level.loadElements(elements);
    }
}
