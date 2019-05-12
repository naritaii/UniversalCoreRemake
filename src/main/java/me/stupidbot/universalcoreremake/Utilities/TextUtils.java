package me.stupidbot.universalcoreremake.Utilities;

import org.apache.commons.lang.WordUtils;

public class TextUtils {
    public static String capitalizeFully(String s) {
        return WordUtils.capitalizeFully(s, new char[] { '_' } ).replaceAll("_", " ");
    }
}
