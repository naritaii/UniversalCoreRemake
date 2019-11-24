package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class WoolBoots extends ItemStack {
    public WoolBoots() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_BOOTS).name("&rWool Boots")
                                .color(Color.fromRGB(245, 246, 250))
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+5")
                                .lore("&7Defense: &a+1")
                                .lore("&7Speed: &a+2%").build(),
                        "HEALTH", "5"),
                "DEFENSE", "1"),
                "SPEED", "0.02"),
                "ITEM", "WOOL_BOOTS"));
    }
}