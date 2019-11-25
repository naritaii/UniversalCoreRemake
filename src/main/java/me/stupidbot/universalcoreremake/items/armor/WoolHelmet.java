package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WoolHelmet extends ItemStack {
    public WoolHelmet() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.WOOL).name("&rWool Helmet")
                                .lore("&7Health: &a+2")
                                .lore("&7Defense: &a+1").build(),
                        "HEALTH", "2"),
                "DEFENSE", "1"),
                "HAT", "1"),
                "NO_STACKING", "1"),
                "ITEM", "WOOL_HELMET"));
    }
}