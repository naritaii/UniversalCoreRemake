package me.stupidbot.universalcoreremake.listeners;

import de.slikey.effectlib.Effect;
import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.effects.FindSlime;
import me.stupidbot.universalcoreremake.managers.BlockMetadataManger;
import me.stupidbot.universalcoreremake.managers.universalplayer.UniversalPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CollectibleSlimesListener implements Listener {
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            BlockMetadataManger bmm = UniversalCoreRemake.getBlockMetadataManager();

            if (bmm.hasMeta(b, "SLIME_NAME") || bmm.hasMeta(b, "SLIME_FIRST_DIALOGUE") ||
                    bmm.hasMeta(b, "SLIME_RECURRING_DIALOGUE")) {
                String name;
                if (bmm.hasMeta(b, "SLIME_NAME"))
                    name = ChatColor.translateAlternateColorCodes('&', bmm.getMeta(b, "SLIME_NAME"));
                else
                    name = "Slime";
                UniversalPlayer up = UniversalCoreRemake.getUniversalPlayerManager().getUniversalPlayer(p);
                if (!up.hasCollectedSlime(b.getLocation())) {
                    String firstDialogue;
                        if (bmm.hasMeta(b, "SLIME_NAME"))
                            firstDialogue = ChatColor.translateAlternateColorCodes('&', bmm.getMeta(b,
                                    "SLIME_FIRST_DIALOGUE"));
                        else
                            firstDialogue = "You found me!";

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&e[NPC] " + name + "&f: " + firstDialogue));
                    up.addCollectedSlime(b.getLocation());

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&bSlimes collected: &d" + up.getSlimesCollected()));
                    Effect eff = new FindSlime(UniversalCoreRemake.getEffectManager());
                    eff.material = Material.SLIME_BALL;
                    eff.targetPlayer = p;
                    eff.setLocation(b.getLocation());
                    eff.run();
                } else {
                    String secondDialogue;
                    if (bmm.hasMeta(b, "SLIME_RECURRING_DIALOGUE"))
                        secondDialogue = ChatColor.translateAlternateColorCodes('&', bmm.getMeta(b,
                                "SLIME_RECURRING_DIALOGUE"));
                    else
                        secondDialogue = "You've already found me...";

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&e[NPC] " + name + "&f: " + secondDialogue));
                }
            }
        }
    }
}