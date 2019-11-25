package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class LeatherBoots extends ItemStack {
    public LeatherBoots() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_BOOTS).name("&rLeather Boots").unbreakable(true)
                                .color(Color.fromRGB(204, 174, 98))
                                .flag(ItemFlag.values())
                                .lore("&7Defense: &a+1")
                                .lore("&7Speed: &a+5%").build(),
                        "DEFENSE", "1"),
                "SPEED", "0.05"),
                "ITEM", "LEATHER_BOOTS"));
    }
}