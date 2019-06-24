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

public class AsyncPlayerChatListener implements Listener {
    // TODO Just like, redo this entire shitty class...

    @EventHandler(priority = EventPriority.MONITOR)
    public void chatFormat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
        int lvl = up.getDataLevel();
        String lvlTag =  PlayerLevelling.levelTag(lvl) + " ";
        String prefix = ChatColor.translateAlternateColorCodes('&',  up.getDataPrefix());
        String chatColor = TextUtils.getChatColor(p);
        String name = p.getName();
        String message = process(e.getMessage(), p);
        ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name);
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder( prefix + name + "\n" +
                        ChatColor.translateAlternateColorCodes(
                                '&', "&7Level: &a" + lvl + " &7[" +
                                        TextUtils.getProgressBar(up.getDataXp(), PlayerLevelling.xpToNextLevel(lvl),
                                                18, "|", "&a", "&8") +
                                        "&7]\n" +
                                        "&7Balance: &a$" +
                                        TextUtils.addCommas(UniversalCoreRemake.getEconomy().getBalance(p)) +
                                        "\n\n" +
                                        "&eClick to message " + up.getDataNamColor() + name
                        )
                ).create());
        BaseComponent[] component = new ComponentBuilder(
                lvlTag + prefix + name)
                .event(he).event(ce)
                .append(chatColor + ": " + message).create();

        e.getRecipients().forEach((Player r) -> r.spigot().sendMessage(component));
        e.setCancelled(true);
    }

    private String process(String s, Player p) { // TODO Make perms consistent and update legacy ones.
        if (p.hasPermission("universalmiscp.chat.emojis"))
            for (TextUtils.Emoji e : TextUtils.Emoji.values())
                s = s.replaceAll("(?i)" + e.getPlaceholder(), TextUtils.escapeRegex(e.getEmoji()));
        if (p.hasPermission("universalcore.admin"))
            s = ChatColor.translateAlternateColorCodes('&', s);

        return s;
    }
}