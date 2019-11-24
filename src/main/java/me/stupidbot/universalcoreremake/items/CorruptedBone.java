package me.stupidbot.universalcoreremake.items;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

class CorruptedBone extends ItemStack {
    public CorruptedBone() {
        super(ItemMetadata.setMeta(
                new ItemBuilder(Material.BONE).name("&dCorrupted Bone").build(),
                "ITEM", "CORRUPTED_BONE"));
    }
}
