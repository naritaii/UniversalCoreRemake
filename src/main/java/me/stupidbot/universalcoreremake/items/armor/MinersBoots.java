package me.stupidbot.universalcoreremake.items.armor;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MinersBoots extends ItemStack {
    public MinersBoots() {
        super(ItemMetadata.setMeta(ItemMetadata.setMeta(
                ItemMetadata.setMeta(
                        new ItemBuilder(Material.LEATHER_BOOTS).name("&rMiner's Boots").unbreakable(true)
                                .color(Color.fromRGB(196, 69, 105))
                                .flag(ItemFlag.values())
                                .lore("&7Defense: &a+3")
                                .lore("&7Speed: &c-1%")
                                .lore("")
                                .lore("&b&lFull Set Bonus:&6 +20% mining speed").build(),
                        "DEFENSE", "3"),
                "SPEED", "-0.01"),
                "ITEM", "MINERS_BOOTS"));
    }

    // Listener in MiningManager
}