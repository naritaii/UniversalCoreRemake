package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class SlimeChestplate extends ItemStack {
    public SlimeChestplate() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_CHESTPLATE).name("&bSlime Chestplate").flag(ItemFlag.values())
                                .color(Color.fromRGB(85, 255, 255))
                                .lore("&7Health: &a+20")
                                .lore("&7Defense: &a+1")
                                .lore("")
                                .lore("&b&lFull Set Bonus:&6 Constant Jump Boost I & +20% Speed").build(),
                        "HEALTH", "20"),
                "DEFENSE", "1"),
                "ITEM", "SLIME_CHESTPLATE"));
    }

    // Listener in StatsManager
}