package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class WoolChestplate extends ItemStack {
    public WoolChestplate() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_CHESTPLATE).name("&rWool Chestplate")
                                .color(Color.fromRGB(245, 246, 250))
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+11")
                                .lore("&7Defense: &a+4").build(),
                        "HEALTH", "11"),
                "DEFENSE", "4"),
                "ITEM", "WOOL_CHESTPLATE"));
    }
}