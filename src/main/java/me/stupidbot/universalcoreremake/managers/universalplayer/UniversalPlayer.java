package me.stupidbot.universalcoreremake.managers.universalplayer;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
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


    public int getObjectiveData(String id, String sub) {
        return pFile.getInt("Objective." + id + "." + sub);
    }

    public int setObjectiveData(String id, String sub, int data) {
        pFile.set("Objective." + id + "." + sub, data);
        return data;
    }

    public String removeObjectiveData(String id) {
        pFile.set("Objective." + id, null);
        return id;
    }

    public List<String> getCompletedObjectives() {
        return pFile.getStringList("Objective.Completed");
    }

    public List<String> addCompletedObjective(String id) {
        List<String> completed = getCompletedObjectives();
        completed.add(id);
        pFile.set("Objective.Completed", completed);
        return completed;
    }

    public List<String> getSelectedObjectives() {
        return pFile.getStringList("Objective.Selected");
    }

    public List<String> addSelectedObjective(String id) {
        List<String> selected = getCompletedObjectives();
        selected.add(id);
        pFile.set("Objective.Selected", selected);
        return selected;
    }

    public List<String> removeSelectedObjective(String id) {
        List<String> selected = getCompletedObjectives();
        selected.remove(id);
        pFile.set("Objective.Selected", selected);
        return selected;
    }
}