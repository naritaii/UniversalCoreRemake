package me.stupidbot.universalcoreremake.Utilities;

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

    static void sendSubtitle(Player p, String msg, int fadeIn, int stayTime, int fadeOut) {
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

    static String toRoman(int mInt) {
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
    static void sendCenteredMessage(Player player, String message) {
        if (message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '\u00a7') {
                previousCode = true;
            } else if( previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
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
        while(compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    enum DefaultFontInfo {
        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PERENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        DEFAULT('a', 4);

        private final char character;
        private final int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        char getCharacter(){
            return this.character;
        }

        int getLength(){
            return this.length;
        }

        int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) return this.getLength();
            return this.length + 1;
        }

        static DefaultFontInfo getDefaultFontInfo(char c) {
            for( DefaultFontInfo dFI : DefaultFontInfo.values()){
                if (dFI.getCharacter() == c) return dFI;
            }
            return DefaultFontInfo.DEFAULT;
        }
    }
}