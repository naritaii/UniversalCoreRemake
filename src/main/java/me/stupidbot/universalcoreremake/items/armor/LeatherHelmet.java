package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class LeatherHelmet extends ItemStack {
    public LeatherHelmet() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_HELMET).name("&rLeather Helmet")
                                .color(Color.fromRGB(204, 174, 98))
                                .flag(ItemFlag.values())
                                .lore("&7Defense: &a+1").build(),
                        "DEFENSE", "1"),
                "ITEM", "LEATHER_HELMET"));
    }
}