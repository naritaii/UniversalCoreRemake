package me.stupidbot.universalcoreremake.items;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InfestedFire extends ItemStack {
    public InfestedFire() {
        super(ItemMetadata.setMeta(
                new ItemBuilder(new ItemStack(Material.BLAZE_POWDER)).name("&6Infested Fire").build(),
                "ITEM", "INFESTED_FIRE"));
    }
}
