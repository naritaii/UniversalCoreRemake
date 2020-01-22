package me.stupidbot.universalcoreremake.commands;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.utilities.TextUtils;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Dangle {
    private final Set<UUID> dangling = new HashSet<>();

    @SuppressWarnings("SameReturnValue")
    boolean execute(CommandSender s) {
        if (s.hasPermission("universalcore.dangle"))
            if (s instanceof Player) {
                Player p = (Player) s;
                if (dangling.contains(p.getUniqueId()))
                    dangling.remove(p.getUniqueId());
                else if (p.getItemInHand().getType() != Material.AIR) {
                    ItemStack item = p.getItemInHand();
                    Hologram hologram = HologramsAPI.createHologram(UniversalCoreRemake.getInstance(), p.getLocation());

                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName())
                        hologram.appendTextLine(ChatColor.GREEN + item.getItemMeta().getDisplayName());
                    else
                        hologram.appendTextLine(ChatColor.GREEN + TextUtils.capitalizeFully(item.getType().toString()));

                    hologram.appendTextLine("");
                    hologram.appendItemLine(item);
                    hologram.appendTextLine("");

                    if (item.hasItemMeta())
                        for (String l : item.getItemMeta().getLore())
                            if (!l.startsWith(ItemMetadata.hiddenPl))
                                hologram.appendTextLine(l);

                    dangling.add(p.getUniqueId());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aDangling item!"));
                    new BukkitRunnable() {
                        int timer = 0;

                        @Override
                        public void run() {
                            if (timer < 20 * 60 && p.isOnline() && dangling.contains(p.getUniqueId()) && !p.isSneaking()) {
                                Location inFront = p.getEyeLocation().add(p.getLocation().getDirection().multiply(2.5));
                                hologram.teleport(inFront.add(0, hologram.getHeight() / 3, 0));

                                TextUtils.sendActionbar(p, "&e&lSNEAK TO STOP DANGLING");

                                timer++;
                            } else {
                                dangling.remove(p.getUniqueId());
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&cStopped dangling item!"));
                                hologram.delete();
                                TextUtils.sendActionbar(p, "");
                                this.cancel();
                            }
                        }
                    }.runTaskTimerAsynchronously(UniversalCoreRemake.getInstance(), 0, 1);
                } else
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou're not holding any items!"));
            } else
                s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cOnly players may use this command!"));
        else
            s.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou don't have permission to use this command!"));
        return true;
    }
}