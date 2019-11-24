package me.stupidbot.universalcoreremake.items.axes;

import me.stupidbot.universalcoreremake.utilities.item.ItemBuilder;
import me.stupidbot.universalcoreremake.utilities.item.ItemLevelling;
import me.stupidbot.universalcoreremake.utilities.item.ItemMetadata;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ReinforcedIronAxe extends ItemStack {
    public ReinforcedIronAxe() {
        super(ItemLevelling.updateItem(ItemMetadata.setMeta(ItemMetadata.setMeta(ItemMetadata.setMeta(
                new ItemBuilder(Material.IRON_AXE)
                        .enchantment(Enchantment.DIG_SPEED, 1).unbreakable(true).flag(ItemFlag.values()).build(),
                "CUSTOM_NAME", "&rReinforced Iron Axe"),
                "STAMINA_USE", "2"),
                "ITEM", "REINFORCED_IRON_AXE")));
    }
}
