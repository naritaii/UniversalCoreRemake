package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import me.stupidbot.universalcoreremake.utilities.item.Skull;
import org.bukkit.inventory.ItemStack;

public class SlimeHelmet extends ItemStack {
    public SlimeHelmet() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Skull.getCustomSkull(Skull.SLIME_SANS.getId())).name("&bSlime Helmet")
                                .lore("&7Health: &a+10")
                                .lore("&7Defense: &a+1")
                                .lore("")
                                .lore("&b&lFull Set Bonus:&6 Constant Jump Boost I & +20% Speed").build(),
                        "HEALTH", "10"),
                "DEFENSE", "1"),
                "HAT", "1"),
                "NO_STACKING", "1"),
                "ITEM", "SLIME_HELMET"));
    }

    // Listener in StatsManager
}