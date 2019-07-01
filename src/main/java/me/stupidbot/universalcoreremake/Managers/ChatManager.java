package me.stupidbot.universalcoreremake.Managers;

import me.stupidbot.universalcoreremake.Utilities.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager implements Listener {
    private final Map<UUID, Long> chatCooldowns = new HashMap<>();
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!p.hasPermission("universalcore.nochatcooldown"))
            if (chatCooldowns.containsKey(p.getUniqueId())) {
                int diff = (int) Math.ceil(
                        (System.nanoTime() - chatCooldowns.get(p.getUniqueId())) / 1000000000);
                if (diff < 3) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou must wait &e" + (3 - diff) + "s &cto chat again!\n" +
                                    "&aBypass chat-cooldown by buying a rank at &ebuy.corruptprisons.net"));
                    e.setCancelled(true);
                } else
                    chatCooldowns.put(p.getUniqueId(), System.nanoTime());
            } else
                chatCooldowns.put(p.getUniqueId(), System.nanoTime());

        if (p.hasPermission("universalcore.emojis")) {
            String s = e.getMessage();
            for (TextUtils.Emoji em : TextUtils.Emoji.values())
                s = s.replaceAll("(?i)" + em.getPlaceholder(), TextUtils.escapeRegex(em.getEmoji()) +
                        TextUtils.getChatColor(p));
            e.setMessage(s);
        }
    }
}