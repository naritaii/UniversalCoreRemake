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

    public String setPlayerDataName(String s) {
        pFile.set("Name.Name", s);
        return s;
    }

    public String getPlayerDataName() {
        return (String) pFile.get("Name.Name");
    }

    public String setPlayerDataFirstPlayed(String s) {
        pFile.set("Stats.FirstJoin", s);
        return s;
    }

    public String getPlayerDataFirstPlayed() {
        return (String) pFile.get("Stats.FirstJoin");
    }
    public String setPlayerDataLastPlayed(String s) {
        pFile.set("Stats.LastPlayed", s);
        return s;
    }

    public String getPlayerDataLastPlayed() {
        return (String) pFile.get("Stats.LastPlayed");
    }

    public String setPlayerDataCosmeticIdTrail(String s) {
        pFile.set("Cosmetic.TrailID", s);
        return s;
    }

    public String getPlayerDataCosmeticIdTrail() {
        String s = (String) pFile.get("Cosmetic.TrailID");

        if (s == null)
            s = setPlayerDataCosmeticIdTrail("none");

        return s;
    }
}