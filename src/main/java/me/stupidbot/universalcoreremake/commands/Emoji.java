package me.stupidbot.universalcoreremake.commands;

import me.stupidbot.universalcoreremake.utilities.TextUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

class Emoji {
    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s instanceof Player) {
            Player p = (Player) s;
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&6PREMIUM &ahas access to the following emotes in chat:"));
            Arrays.stream(TextUtils.Emoji.values()).forEach((TextUtils.Emoji e) -> {
                ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, e.getPlaceholder());
                HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                                "&6Click to send the &c&l" +
                                        e.name().replaceAll("_", " ") + "&6 emoji!")
                        ).create());

                p.spigot().sendMessage(new ComponentBuilder(
                        ChatColor.translateAlternateColorCodes('&',
                                "&6" + e.getPlaceholder() + " &r\u279C " + e.getEmoji()))
                        .event(he).event(ce).create());

            });
        } else {
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&6PREMIUM &ahas access to the following emotes in chat:"));
            Arrays.stream(TextUtils.Emoji.values()).forEach((TextUtils.Emoji e) -> s.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "&6" + e.getPlaceholder() + " &r\u279C " + e.getEmoji())));
        }
        return true;
    }
}