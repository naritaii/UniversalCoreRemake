package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MinersChestplate extends ItemStack {
    public MinersChestplate() {
        super(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_CHESTPLATE).name("&rMiner's Chestplate")
                                .color(Color.fromRGB(196, 69, 105))
                                .flag(ItemFlag.values())
                                .lore("&7Defense: &a+15")
                                .lore("")
                                .lore("&b&lFull Set Bonus:&6 +20% mining speed").build(),
                        "DEFENSE", "15"),
                "ITEM", "MINERS_CHESTPLATE"));
    }

    // Listener in MiningManager
}