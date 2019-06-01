package me.stupidbot.universalcoreremake.Utilities.Players;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class UniversalPlayer {

    private final File pFileLoc;
    private final FileConfiguration pFile;

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

    void savePlayerDataFile() {
        try {
            pFile.save(pFileLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int setDataLevel(int i) {
        pFile.set("XP.Level", i);
        return i;
    }

    public int getDataLevel() {
        int i;
        i = pFile.getInt("XP.Level");
        return i;
    }

    public int setDataXp(int i) {
        pFile.set("XP.XP", i);
        return i;
    }

    public int getDataXp() {
        int i;
        i = pFile.getInt("XP.XP");
        return i;
    }

    public int setDataTotalXp(int i) {
        pFile.set("XP.TotalXP", i);
        return i;
    }

    public int getDataTotalXp() {
        int i;
        i = pFile.getInt("XP.TotalXP");
        return i;
    }

    public String setDataName(String s) {
        pFile.set("Name.Name", s);
        return s;
    }

    public String getDataName() {
        return (String) pFile.get("Name.Name");
    }

    public String setDataNameColor(String s) {
        pFile.set("Name.Color", s);
        return s;
    }

    public String getDataNamColor() {
        return (String) pFile.get("Name.Color");
    }

    public String setDataPrefix(String s) {
        pFile.set("Name.Prefix", s);
        return s;
    }

    public String getDataPrefix() {
        return (String) pFile.get("Name.Prefix");
    }

    public String setDataFirstPlayed(String s) {
        pFile.set("Stats.FirstJoin", s);
        return s;
    }

    public String getDataFirstPlayed() {
        return (String) pFile.get("Stats.FirstJoin");
    }

    public String setDataLastPlayed(String s) {
        pFile.set("Stats.LastPlayed", s);
        return s;
    }

    public String getDataLastPlayed() {
        return (String) pFile.get("Stats.LastPlayed");
    }


    public String setDataCosmeticIdTrail(String s) {
        pFile.set("Cosmetic.TrailID", s);
        return s;
    }

    public String getDataCosmeticIdTrail() {
        String s = (String) pFile.get("Cosmetic.TrailID");

        if (s == null)
            s = setDataCosmeticIdTrail("none");

        return s;
    }
}