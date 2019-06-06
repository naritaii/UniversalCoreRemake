package me.stupidbot.universalcoreremake.Managers;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Mining implements Listener {
    public void onPlayerJoin(PlayerJoinEvent e) { // Stop player from breaking blocks so we can handle block breaking
        Player p = e.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 255), true);
    }

    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (e.getAction() == Action.LEFT_CLICK_BLOCK && BlockMetadataManger.hasMetadata(b, "MINEABLE") ) {
        }
    }

    enum MineableBlock {
        RED_SANDSTONE(3d, 3d),
        SANDSTONE(4d, 0d);

        private Double durability;

        /**
         * @param durability Time in seconds block takes to mine with hand and no "modifiers".
         * @param regenerateTime Time in seconds block takes to regenerate.
         */
        MineableBlock(Double durability, Double regenerateTime) {
            this.durability = durability;
        }
    }
}