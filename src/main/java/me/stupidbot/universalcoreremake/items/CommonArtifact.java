package me.stupidbot.universalcoreremake.items;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import me.stupidbot.universalcoreremake.guis.Roller;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

class CommonArtifact extends ItemStack implements Listener {
    public CommonArtifact() {
        super(ItemMetadata.setMeta(
                new ItemBuilder(Material.FIREWORK_CHARGE).name("&a&lCOMMON&e Artifact").build(),
                "ITEM", "ARTIFACT_COMMON"));
    }

    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack i = p.getItemInHand();
            Map<String, String> itemMeta = ItemMetadata.getMeta(i);

            if (itemMeta.containsKey("ITEM") &&
                    itemMeta.get("ITEM").equalsIgnoreCase("ARTIFACT_COMMON")) {
                e.setCancelled(true);
                Roller.getInventory(p, "Common Artifact",
                        UniversalCoreRemake.getRewardManager().commonArtifactRewards).open(p);
            }
        }
    }
}
