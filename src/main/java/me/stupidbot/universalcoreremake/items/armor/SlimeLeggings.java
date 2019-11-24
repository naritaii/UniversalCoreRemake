package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class SlimeLeggings extends ItemStack {
    public SlimeLeggings() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_LEGGINGS).name("&bSlime Leggings").flag(ItemFlag.values())
                                .color(Color.fromRGB(85, 255, 255))
                                .lore("&7Health: &a+13")
                                .lore("&7Defense: &a+1")
                                .lore("")
                                .lore("&b&lFull Set Bonus:&6 Constant Jump Boost I & +20% Speed").build(),
                        "HEALTH", "13"),
                "DEFENSE", "1"),
                "ITEM", "SLIME_LEGGINGS"));
    }

    // Listener in StatsManager
}