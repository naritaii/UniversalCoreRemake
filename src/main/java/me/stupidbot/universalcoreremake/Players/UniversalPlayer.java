package me.stupidbot.universalcoreremake.Players;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class UniversalPlayer {

    private File pFileLoc;
    private FileConfiguration pFile;

    UniversalPlayer(File pFileLoc, FileConfiguration pFile) {
        this.pFileLoc = pFileLoc;
        this.pFile = pFile;
    }

    public File getPlayerDataFile() {
        return pFileLoc;
    }

    public FileConfiguration loadPlayerDataFile() {
        return pFile;
    }

    public void savePlayerDataFile() {
        try {
            pFile.save(pFileLoc);
        } catch (IOException e) { // TODO Properly handle errors
            e.printStackTrace();
        }
    }

    public void setPlayerDataName(String s) {
        pFile.set("Name.Name", s);
    }

    public String getPlayerDataName() {
        return (String) pFile.get("Name.Name");
    }

    public void setPlayerDataFirstPlayed(String s) {
        pFile.set("Stats.FirstJoin", s);
    }

    public String getPlayerDataFirstPlayed() {
        return (String) pFile.get("Stats.FirstJoin");
    }
    public void setPlayerDataLastPlayed(String s) {
        pFile.set("Stats.LastPlayed", s);
    }

    public String getPlayerDataLastPlayed() {
        return (String) pFile.get("Stats.LastPlayed");
    }
}