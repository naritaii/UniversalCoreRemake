package me.stupidbot.universalcoreremake.items.pickaxes;

import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class RedSandstonePickaxe extends ItemStack {
    public RedSandstonePickaxe() {
        super(ItemLevelling.updateItem(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                new ItemBuilder(Material.WOOD_PICKAXE)
                        .enchantment(UniversalEnchantment.SANDSTONE_LOVER).unbreakable(true).flag(ItemFlag.values()).build(),
                "CUSTOM_NAME", "&cRed Sandstone Pickaxe"),
                "STAMINA_USE", "1"),
                "ITEM", "RED_SANDSTONE_PICKAXE")));
    }
}
