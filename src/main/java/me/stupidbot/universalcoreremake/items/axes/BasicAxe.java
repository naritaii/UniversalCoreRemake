package me.stupidbot.universalcoreremake.items.axes;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class BasicAxe extends ItemStack {
    public BasicAxe() {
        super(ItemLevelling.updateItem(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                new ItemBuilder(Material.WOOD_AXE)
                        .unbreakable(true).flag(ItemFlag.values()).build(),
                "CUSTOM_NAME", "&rBasic Axe"),
                "LORE", "&7&oBasic."),
                "STAMINA_USE", "1"),
                "ITEM", "BASIC_AXE")));
    }
}
