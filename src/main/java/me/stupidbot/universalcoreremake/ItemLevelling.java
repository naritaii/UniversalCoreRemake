package me.stupidbot.universalcoreremake;

public class ItemLevelling {
    public static int xpToNextLevel(int lvl) {
        return (int) ((Math.pow(++lvl, 2) * 2));
    }
}