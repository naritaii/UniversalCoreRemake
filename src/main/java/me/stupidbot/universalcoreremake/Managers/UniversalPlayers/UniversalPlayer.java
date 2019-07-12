package me.stupidbot.universalcoreremake.Managers.UniversalPlayers;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

@SuppressWarnings("SameParameterValue")
public class UniversalPlayer {
    private final File pFileLoc;
    private final FileConfiguration pFile;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");

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

    public static SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public boolean firstJoin() {
        return getLastPlayed() == null;
    }

    void savePlayerDataFile() {
        try {
            pFile.save(pFileLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int setLevel(int i) {
        pFile.set("XP.Level", i);
        return i;
    }

    public int getLevel() {
        int i;
        i = pFile.getInt("XP.Level");
        return i;
    }

    public int setXp(int i) {
        pFile.set("XP.XP", i);
        return i;
    }

    public int getXp() {
        int i;
        i = pFile.getInt("XP.XP");
        return i;
    }

    public int setTotalXp(int i) {
        pFile.set("XP.TotalXP", i);
        return i;
    }

    public int getTotalXp() {
        int i;
        i = pFile.getInt("XP.TotalXP");
        return i;
    }

    String setName(String s) {
        pFile.set("Name.Name", s);
        return s;
    }

    public String getName() {
        return pFile.getString("Name.Name");
    }

    String setNameColor(String s) {
        pFile.set("Name.Color", s);
        return s;
    }

    public String getNameColor() {
        return pFile.getString("Name.Color");
    }

    String setPrefix(String s) {
        pFile.set("Name.Prefix", s);
        return s;
    }

    public String getPrefix() {
        return pFile.getString("Name.Prefix");
    }

    String setFirstPlayed(String s) {
        pFile.set("Stats.FirstJoin", s);
        return s;
    }

    String getFirstPlayed() {
        return pFile.getString("Stats.FirstJoin");
    }

    String setDataLastPlayed(String s) {
        pFile.set("Stats.LastPlayed", s);
        return s;
    }

    public String getLastPlayed() {
        return pFile.getString("Stats.LastPlayed");
    }

    public int getBlocksMined() {
        return pFile.getInt("Stats.BlocksMined");
    }

    public int setBlocksMined(int i) {
        pFile.set("Stats.BlocksMined", i);
        return i;
    }


    private String setCosmeticIdTrail(String s) {
        pFile.set("Cosmetic.TrailID", s);
        return s;
    }

    public String getCosmeticIdTrail() {
        String s = pFile.getString("Cosmetic.TrailID");

        if (s == null)
            s = setCosmeticIdTrail("none");

        return s;
    }


    public int getStamina() {
        return pFile.getInt("Stamina");
    }

    public int setStamina(int i) {
        pFile.set("Stamina", i);
        return i;
    }

    public String getQuestData(String id, String sub) {
        return pFile.getString("Quest." + id + "." + sub);
    }

    public String setQuestData(String id, String sub, String data) {
        pFile.set("Quest." + id + "." + data, data);
        return data;
    }
}