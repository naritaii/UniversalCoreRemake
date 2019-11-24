package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class LeatherLeggings extends ItemStack {
    public LeatherLeggings() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_LEGGINGS).name("&rLeather Leggings")
                                .color(Color.fromRGB(204, 174, 98))
                                .flag(ItemFlag.values())
                                .lore("&7Defense: &a+3").build(),
                        "DEFENSE", "3"),
                "ITEM", "LEATHER_LEGGINGS"));
    }
}