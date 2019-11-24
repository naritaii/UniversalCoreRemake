package me.stupidbot.universalcoreremake.items.pickaxes;

import me.stupidbot.universalcoreremake.enchantments.UniversalEnchantment;
import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class IronOrePickaxe extends ItemStack {
    public IronOrePickaxe() {
        super(ItemLevelling.updateItem(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                new ItemBuilder(Material.WOOD_PICKAXE)
                        .enchantment(UniversalEnchantment.IRON_LOVER).unbreakable(true).flag(ItemFlag.values()).build(),
                "CUSTOM_NAME", "&cIron Ore Pickaxe"),
                "STAMINA_USE", "3"),
                "ITEM", "IRON_ORE_PICKAXE")));
    }
}
