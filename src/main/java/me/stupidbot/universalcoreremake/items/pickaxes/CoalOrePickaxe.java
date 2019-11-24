package me.stupidbot.universalcoreremake.items.pickaxes;

import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CoalOrePickaxe extends ItemStack {
    public CoalOrePickaxe() {
        super(ItemLevelling.updateItem(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                new ItemBuilder(Material.WOOD_PICKAXE)
                        .enchantment(UniversalEnchantment.COAL_LOVER).unbreakable(true).flag(ItemFlag.values()).build(),
                "CUSTOM_NAME", "&cCoal Ore Pickaxe"),
                "STAMINA_USE", "2"),
                "ITEM", "COAL_ORE_PICKAXE")));
    }
}
