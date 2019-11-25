package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class LeatherChestplate extends ItemStack {
    public LeatherChestplate() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_CHESTPLATE).name("&rLeather Chestplate").unbreakable(true)
                                .color(Color.fromRGB(204, 174, 98))
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+4")
                                .lore("&7Defense: &a+4").build(),
                        "HEALTH", "4"),
                "DEFENSE", "4"),
                "ITEM", "LEATHER_CHESTPLATE"));
    }
}