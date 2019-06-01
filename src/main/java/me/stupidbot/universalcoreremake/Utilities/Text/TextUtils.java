package me.stupidbot.universalcoreremake.Utilities.Text;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class TextUtils {
    public static String capitalizeFully(String s) {
        return WordUtils.capitalizeFully(s, new char[] { '_' } ).replaceAll("_", " ");
    }

    public static String addCommas(int i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }

    public static String addCommas(double d) {
        return NumberFormat.getNumberInstance(Locale.US).format(d);
    }

    public static void sendTitle(Player p, String msg, int fadeIn, int stayTime, int fadeOut) {
        /* PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&',
                        "{\"text\": \"" + msg + "\"}")), fadeIn, stayTime, fadeOut);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title); */
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(
                ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + msg + "\"}"));
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stayTime, fadeOut);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
    }

    public static void sendSubtitle(Player p, String msg, int fadeIn, int stayTime, int fadeOut) {
       /* PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&',
                        "{\"text\": \"" + msg + "\"}")), fadeIn, stayTime, fadeOut);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title); */
        IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a(
                ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + msg + "\"}"));
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stayTime, fadeOut);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
    }

    public static String toRoman(int mInt) {
        String[] rnChars = { "M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I" };
        int[] rnVals = { 1000, 900, 500, 100, 90, 50, 10, 9, 5, 1 };
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < rnVals.length; i++) {
            int numberInPlace = mInt / rnVals[i];
            if (numberInPlace != 0) {
                retVal.append((numberInPlace == 4) && (i > 0) ? rnChars[i] + rnChars[(i - 1)]
                        : new String(new char[numberInPlace]).replace("\000", rnChars[i]));
                mInt %= rnVals[i];
            }
        }
        return retVal.toString();
    }

    // private final static int CENTER_PX = 154;
    public static void sendCenteredMessage(Player player, String message) {
        if(message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '\u00a7'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        int CENTER_PX = 154;
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }
}
