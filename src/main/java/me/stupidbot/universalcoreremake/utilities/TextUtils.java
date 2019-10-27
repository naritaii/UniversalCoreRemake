package me.stupidbot.universalcoreremake.utilities;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class TextUtils {
    public static String capitalizeFully(String s) {
        return WordUtils.capitalizeFully(s, new char[]{'_'}).replaceAll("_", " ")
                .replaceAll("~", ""); // ~ to force capital, good for roman numerals etc
    }

    public static String addCommas(int i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }

    public static String addCommas(double d) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(d).substring(1);
    }

    public static String getChatColor(OfflinePlayer p) {
        if (p.getUniqueId().toString().equals("e8584196-baf0-4b9d-8146-2d438cafa99a")) // HaydenPlus' chat color
            return ChatColor.GREEN.toString();
        else if (!UniversalCoreRemake.getPermissions().playerHas(
                null, p, "chatformat.white"))
            return ChatColor.GRAY.toString();
        else
            return ChatColor.WHITE.toString();
    }

    public static String toBallNumbers(String s) {
        StringBuilder sb = new StringBuilder();
        boolean skipNext = false;
        for (char c : s.toCharArray())
            if (!skipNext)
                switch (c) { // There's no supported unicode for 0 in a ball afaik so this only works for 1-9
                    default:
                        sb.append(c);
                        break;
                    case ChatColor.COLOR_CHAR:
                        skipNext = true;
                        break;
                    case '1':
                        sb.append('\u2776');
                        break;
                    case '2':
                        sb.append('\u2777');
                        break;
                    case '3':
                        sb.append('\u2778');
                        break;
                    case '4':
                        sb.append('\u2779');
                        break;
                    case '5':
                        sb.append('\u277a');
                        break;
                    case '6':
                        sb.append('\u277b');
                        break;
                    case '7':
                        sb.append('\u277c');
                        break;
                    case '8':
                        sb.append('\u277d');
                        break;
                    case '9':
                        sb.append('\u277e');
                        break;
                }
            else {
                sb.append(c);
                skipNext = false;
            }

        return sb.toString();
    }

    public static String toBallNumber(int i) {
        return toBallNumbers(i + "");
    }

    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13)
            return addCommas(day) + "th";
        switch (day % 10) {
            case 1:
                return addCommas(day) + "st";
            case 2:
                return addCommas(day) + "nd";
            case 3:
                return addCommas(day) + "rd";
            default:
                return addCommas(day) + "th";
        }
    }

    public static String secondsToString(long seconds) {
        long p1 = seconds % 60;
        long p2 = seconds / 60;
        long p3 = p2 % 60;

        p2 = p2 / 60;

        return addCommas((int) p2) + "h, " + p3 + "m, " + p1 + "s";
    }

    public static void sendTitle(Player p, String msg, int fadeIn, int stayTime, int fadeOut) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(
                ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + msg + "\"}"));
        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stayTime, fadeOut);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
    }

    public static void sendSubtitle(Player p, String msg, int fadeIn, int stayTime, int fadeOut) {
        IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a(
                ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + msg + "\"}"));
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stayTime, fadeOut);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
    }

    public static void sendActionbar(Player p, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(
                ChatColor.translateAlternateColorCodes('&', "{\"text\":\"" + message + "\"}"));
        PacketPlayOutChat packet = new PacketPlayOutChat(icbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public static String toRoman(int mInt) {
        String[] rnChars = {"M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] rnVals = {1000, 900, 500, 100, 90, 50, 10, 9, 5, 1};
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

    public static String getProgressBar(int current, int max, int totalBars, String symbol, String completedColor,
                                        String notCompletedColor) {
        float percent = (float) current / max;

        int progressBars = (int) (totalBars * percent);

        int leftOver = (totalBars - progressBars);

        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.translateAlternateColorCodes('&', completedColor));
        for (int i = 0; i < progressBars; i++) {
            sb.append(symbol);
        }
        sb.append(ChatColor.translateAlternateColorCodes('&', notCompletedColor));
        for (int i = 0; i < leftOver; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }

    public static String centerMessage(String message, int CENTER_PX) {
        if (message == null || message.equals(""))
            return "";
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '\u00a7') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }

    public static void sendCenteredMessage(Player p, String message) {
        p.sendMessage(centerMessage(message, 154));
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

        char getCharacter() {
            return this.character;
        }

        int getLength() {
            return this.length;
        }

        int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) return this.getLength();
            return this.length + 1;
        }

        static DefaultFontInfo getDefaultFontInfo(char c) {
            for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
                if (dFI.getCharacter() == c) return dFI;
            }
            return DefaultFontInfo.DEFAULT;
        }
    }

    public static String escapeRegex(String str) {
        return str.replaceAll("[<(\\[{\\\\^\\-=$!|\\]})?*+.>]",
                "\\\\$0");
    }

    public enum Emoji {
        TABLE_FLIP(":tableflip:",
                "&c\uFF08\u256F\u00B0\u25A1\u00B0\uFF09\u256F&f\uFE35 &7\u253B\u2501\u253B"),
        REAL_SMOOTH(":realsmooth:",
                "&e(\u3065 \uFFE3 \u00B3\uFFE3)\u3065&6\u24C8\u24C2\u24C4\u24C4\u24C9\u24BD"),
        SHRUG(":shrug:", "&e\u00AF\\_(\u30C4)_/\u00AF"),
        UWU(":uwu:", "&dU&5w&dU"),
        WUT(":wut:", "&b\u2609&e_&b\u2609"),
        SRS(":srs:", "&c(\u0CA0_\u0CA0)"),
        WOOF(":woof:", "&e(\u1D54\u1D25\u1D54)"),
        WAVE(":wave:", "&d( \uFF9F\u25E1\uFF9F)/"),
        DAB(":dab:", "&9<o/"),
        CHEER(":cheer:", "&ed(^o^)b"),
        THANKS(":thanks:", "&e\\(^-^)/"),
        HEART(":heart:", "&c\u2764"),
        STAR(":star:", "&6\u272F"),
        CROSS(":cross:", "&6\u271E"),
        PEACE(":peace:", "&a\u270C"),
        ONE_TWO_THREE(":123:", "&a1&e2&c3"),
        OOF(":oof:", "&c&lOOF"),
        ONE_HUNDRED(":100:", "&c&n100");

        private final String placeholder;
        private final String emoji;

        Emoji(String placeholder, String emoji) {
            this.placeholder = placeholder;
            this.emoji = ChatColor.translateAlternateColorCodes('&', emoji);
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public String getEmoji() {
            return emoji;
        }
    }
}