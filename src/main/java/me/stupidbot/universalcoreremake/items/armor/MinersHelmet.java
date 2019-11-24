package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MinersHelmet extends ItemStack {
    public MinersHelmet() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_HELMET).name("&rMiner's Chestplate")
                                .color(Color.fromRGB(196, 69, 105))
                                .flag(ItemFlag.values())
                                .lore("&7Defense: &a+2")
                                .lore("")
                                .lore("&b&lFull Set Bonus:&6 +20% mining speed").build(),
                        "DEFENSE", "2"),
                "ITEM", "MINERS_HELMET"));
    }

    // Listener in MiningManager
}