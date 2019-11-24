package me.stupidbot.universalcoreremake.items.pickaxes;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CorruptPickaxe extends ItemStack {
    public CorruptPickaxe() {
        super(ItemLevelling.updateItem(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                new ItemBuilder(Material.IRON_PICKAXE)
                        .enchantment(Enchantment.DIG_SPEED, 1).unbreakable(true).flag(ItemFlag.values()).build(),
                "CUSTOM_NAME", "&rCorrupted Pickaxe"),
                "STAMINA_USE", "3"),
                "ITEM", "CORRUPTED_PICKAXE")));
    }
}
