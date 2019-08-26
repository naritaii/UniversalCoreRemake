package me.stupidbot.universalcoreremake.managers.leaderboard;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

class LeaderboardManager {
    LeaderboardManager() {

    }

    public <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = Comparator.comparing(map::get);
        Map<K, V> sortedByValues = new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
}
