package me.stupidbot.universalcoreremake.items;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CorruptedShard extends ItemStack {
    public CorruptedShard() {
        super(ItemMetadata.setMeta(
                new ItemBuilder(new ItemStack(Material.PRISMARINE_SHARD)).name("&dCorrupted Shard").build(),
                "ITEM", "CORRUPTED_SHARD"));
    }
}
