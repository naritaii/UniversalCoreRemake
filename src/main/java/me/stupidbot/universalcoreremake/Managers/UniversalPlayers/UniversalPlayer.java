package me.stupidbot.universalcoreremake.Managers.UniversalPlayers;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class UniversalPlayer {
    private final File pFileLoc;
    private final FileConfiguration pFile;
    private final SimpleDateFormat simpleDateFormat;

    UniversalPlayer(File pFileLoc, FileConfiguration pFile) {
        this.pFileLoc = pFileLoc;
        this.pFile = pFile;
        simpleDateFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
    }

    public File getPlayerDataFile() {
        return pFileLoc;
    }

    public FileConfiguration loadPlayerDataFile() {
        return pFile;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public boolean firstJoin() {
        return getDataLastPlayed() == null;
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

    String setDataName(String s) {
        pFile.set("Name.Name", s);
        return s;
    }

    public String getDataName() {
        return pFile.getString("Name.Name");
    }

    String setDataNameColor(String s) {
        pFile.set("Name.Color", s);
        return s;
    }

    public String getDataNamColor() {
        return pFile.getString("Name.Color");
    }

    String setDataPrefix(String s) {
        pFile.set("Name.Prefix", s);
        return s;
    }

    public String getDataPrefix() {
        return pFile.getString("Name.Prefix");
    }

    String setDataFirstPlayed(String s) {
        pFile.set("Stats.FirstJoin", s);
        return s;
    }

    String getDataFirstPlayed() {
        return pFile.getString("Stats.FirstJoin");
    }

    String setDataLastPlayed(String s) {
        pFile.set("Stats.LastPlayed", s);
        return s;
    }

    public String getDataLastPlayed() {
        return pFile.getString("Stats.LastPlayed");
    }

    public int getDataBlocksMined() {
        return pFile.getInt("Stats.BlocksMined");
    }

    public int setDataBlocksMined(int i) {
        pFile.set("Stats.BlocksMined", i);
        return i;
    }


    private String setDataCosmeticIdTrail(String s) {
        pFile.set("Cosmetic.TrailID", s);
        return s;
    }

    public String getDataCosmeticIdTrail() {
        String s = pFile.getString("Cosmetic.TrailID");

        if (s == null)
            s = setDataCosmeticIdTrail("none");

        return s;
    }


    public int getDataStamina() {
        return pFile.getInt("Stamina");
    }

    public int setDataStamina(int i) {
        pFile.set("Stamina", i);
        return i;
    }
}