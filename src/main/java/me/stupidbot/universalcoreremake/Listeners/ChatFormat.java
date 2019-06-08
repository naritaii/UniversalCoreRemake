package me.stupidbot.universalcoreremake.Listeners;

import me.stupidbot.universalcoreremake.Managers.UniversalPlayers.UniversalPlayer;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.Utilities.PlayerLevelling;
import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void asyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        String prefix = ChatColor.translateAlternateColorCodes('&',  up.getDataPrefix());
        String chatColor = ChatColor.translateAlternateColorCodes('&',
                p.hasPermission("universalcoreremake.chatcolor.white") ? "&f" : "&7");
        String name = p.getName();
        int lvl = up.getDataLevel();
        ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name);
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder( prefix + name + "\n" +
                        ChatColor.translateAlternateColorCodes(
                                '&', "&7Level: &a" + lvl + " (" +
                                        TextUtils.addCommas(up.getDataXp()) + "/" +
                                        TextUtils.addCommas(PlayerLevelling.xpToNextLevel(lvl)) + " XP)" + "\n" +
                                        "&7Balance: &a$" +
                                        TextUtils.addCommas(UniversalCoreRemake.getEconomy().getBalance(p)) +
                                        "\n\n" +
                                        "&eClick to message " + up.getDataNamColor() + name
                        )
                ).create());

        BaseComponent[] component = new ComponentBuilder(
                prefix + name)
                    .event(he).event(ce)
                .append(chatColor + ": " + e.getMessage()).create();

        e.getRecipients().forEach((Player r) -> r.spigot().sendMessage(component));
        e.setCancelled(true);
    }
}
