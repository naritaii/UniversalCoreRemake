package me.stupidbot.universalcoreremake.Managers;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class MiningManager implements Listener {
    private final Map<Player, Block> miningPlayers = new HashMap<>();
    private BukkitTask miningTask;

    public void onPlayerJoin(PlayerJoinEvent e) { // Stop player from breaking blocks so we can handle block breaking
        Player p = e.getPlayer();
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 255), true);
    }

    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (e.getAction() == Action.LEFT_CLICK_BLOCK && b.getType() != Material.BARRIER &&
                UniversalCoreRemake.getBlockMetadataManager().hasMetadata(b, "MINEABLE"))
            addMiningPlayer(p, b);
    }

    private void addMiningPlayer(Player p, Block b) {
        miningPlayers.put(p, b);
    }

    private void removeMiningPlayer(Player p) {
        miningPlayers.remove(p);
    }

    public void initialize() {
        Map<Player, Integer> timer = new HashMap<>();
        miningTask = new BukkitRunnable() {
            @Override
            public void run() {
                miningPlayers.forEach((Player p, Block b) -> {

                    timer.put(p, timer.getOrDefault(p, 0) + 1);
                    removeMiningPlayer(p);
                });
            }
        }.runTaskTimerAsynchronously(UniversalCoreRemake.getInstance(), 0, 1);
    }

    public void disable() {
        miningTask.cancel();
        miningPlayers.forEach((Player p, Block b) -> {
            b.getChunk().load();
            if (b.getLocation().getBlock().getType() == Material.BARRIER)
                b.getLocation().getBlock().setType(b.getType());
            miningPlayers.remove(p);
        });
    }

    enum MineableBlock {
        RED_SANDSTONE(3d, 3d, Material.SANDSTONE, 0.05f),
        SANDSTONE(4d, 0d, Material.RED_SANDSTONE, 0.5f);

        private final Double durability;
        private final Double regenerateTime;
        private final Material enhanceBlock;
        private final Float enchantChance;

        /**
         * @param durability Time in seconds block takes to mine with hand and no "modifiers".
         * @param regenerateTime Time in seconds block takes to regenerate.
         * @param enhanceBlock Block to turn into when enhanced.
         * @param enhanceChance Chance of turning block into enhanceBlock.
         */
        MineableBlock(Double durability, Double regenerateTime, Material enhanceBlock, Float enhanceChance) {
            this.durability = durability;
            this.regenerateTime = regenerateTime;
            this.enhanceBlock = enhanceBlock;
            this.enchantChance = enhanceChance;
        }
    }
}