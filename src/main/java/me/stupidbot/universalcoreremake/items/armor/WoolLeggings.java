package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class WoolLeggings extends ItemStack {
    public WoolLeggings() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_LEGGINGS).name("&rWool Leggings").unbreakable(true)
                                .color(Color.fromRGB(245, 246, 250))
                                .flag(ItemFlag.values())
                                .lore("&7Health: &a+7")
                                .lore("&7Defense: &a+3").build(),
                        "HEALTH", "7"),
                "DEFENSE", "3"),
                "ITEM", "WOOL_LEGGINGS"));
    }
}