package me.stupidbot.universalcoreremake.managers.universalplayer;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public class UniversalPlayer {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
    private final FileConfiguration pFile;
    private final String pFileLoc;

    UniversalPlayer(FileConfiguration pFile, String pFileLoc) {
        this.pFile = pFile;
        this.pFileLoc = pFileLoc;
    }

/*    public FileConfiguration loadPlayerDataFile() {
        return pFile;
    }*/

    public static SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public boolean firstJoin() {
        return getLastPlayed() == null;
    }

    public UUID getUuid() {
        return UUID.fromString(StringUtils.substringBetween(pFileLoc, File.separator, ".yml"));
    }

    void savePlayerDataFile() {
        try {
            pFile.save(pFileLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double incrementTotalMoney(double d) {
        d += getTotalMoney();
        pFile.set("TotalMoney", d);
        return d;
    }

    public double getTotalMoney() {
        return pFile.getDouble("TotalMoney");
    }

    public int setLevel(int i) {
        pFile.set("XP.Level", i);
        return i;
    }

    public int getLevel() {
        return pFile.getInt("XP.Level");
    }

    public int setXp(int i) {
        pFile.set("XP.XP", i);
        return i;
    }

    public int getXp() {
        return pFile.getInt("XP.XP");
    }

    public int setTotalXp(int i) {
        pFile.set("XP.TotalXP", i);
        return i;
    }

    public int getTotalXp() {
        return pFile.getInt("XP.TotalXP");
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

    long setJoinNumber(long l) {
        pFile.set("Stats.JoinNumber", l);
        return l;
    }

    long getJoinNumber() {
        return pFile.getLong("Stats.JoinNumber");
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

    public List<String> removeCompletedObjective(String id) {
        List<String> completed = getCompletedObjectives();
        completed.remove(id);
        if (completed.isEmpty())
            completed = null;
        pFile.set("Objective.Completed", completed);
        return completed;
    }

    public List<String> getSelectedObjectives() {
        return pFile.getStringList("Objective.Selected");
    }

    public List<String> addSelectedObjective(String id) {
        List<String> selected = getSelectedObjectives();
        selected.add(id);
        pFile.set("Objective.Selected", selected);
        return selected;
    }

    public List<String> removeSelectedObjective(String id) {
        List<String> selected = getSelectedObjectives();
        selected.remove(id);
        if (selected.isEmpty())
            selected = null;
        pFile.set("Objective.Selected", selected);
        return selected;
    }

    public List<String> getCollectedSlimes() {
        return pFile.getStringList("Slimes.Collected");
    }

    public Location addCollectedSlime(Location loc) {
        List<String> slimes = getCollectedSlimes();
        slimes.add(loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_"
                + loc.getBlockZ());
        pFile.set("Slimes.Collected", slimes);
        return loc;
    }

    public boolean hasCollectedSlime(Location loc) {
        return getCollectedSlimes().contains(loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_"
                + loc.getBlockZ());
    }

    public int getSlimesCollected() {
        return getCollectedSlimes().size();
    }
}
